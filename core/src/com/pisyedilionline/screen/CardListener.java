package com.pisyedilionline.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.pisyedilionline.actor.Card;
import com.pisyedilionline.message.Opcode;
import org.jetbrains.annotations.NotNull;

public class CardListener extends DragListener
{
	private final GameScreen screen;

	private float startX, startY;

	public CardListener(final GameScreen screen)
	{
		this.screen = screen;
	}

	@Override
	public void dragStart(@NotNull InputEvent event, float x, float y, int pointer)
	{
		Card card = (Card) event.getTarget();
		startX = card.getX();
		startY = card.getY();
	}

	@Override
	public void drag(@NotNull InputEvent event, float x, float y, int pointer)
	{
		Card card = (Card) event.getTarget();

		card.moveBy(x - card.getWidth() / 2, y - card.getHeight() / 2);
	}

	@Override
	public void dragStop(@NotNull InputEvent event, float x, float y, int pointer)
	{
		float stageX = event.getStageX(), stageY = event.getStageY();

		Card card = (Card) event.getTarget();

		if (stageX > 70 && stageX < 100 && stageY > 35 && stageY < 65)
		{
			screen.getPlayer().playCard(card);

			screen.game.nakama.getSocket().sendMatchData(screen.game.matchId,
					Opcode.PLAY_CARD.id, Integer.toString(card.getOrder()).getBytes());


			screen.getPlayer().disableHand();
			screen.getDeck().clearListeners();
		}
		else
		{
			card.addAction(Actions.moveTo(startX, startY, 0.1f));
		}
	}
}

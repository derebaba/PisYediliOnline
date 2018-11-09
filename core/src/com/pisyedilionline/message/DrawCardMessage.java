package com.pisyedilionline.message;

import java.util.List;

public class DrawCardMessage
{
	private int[] drawnCards;

	public int[] getdrawnCards() {
		return drawnCards;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (int i: drawnCards) {
			sb.append("card:").append(i).append(", ");
		}
		return sb.toString();
	}
}

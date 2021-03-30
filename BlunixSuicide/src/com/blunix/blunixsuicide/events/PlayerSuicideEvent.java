package com.blunix.blunixsuicide.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.blunix.blunixsuicide.models.SuicideMethod;

public class PlayerSuicideEvent extends Event {
	/*
	 * Triggered when the player types /suicide
	 */
	
	private static final HandlerList HANDLERS = new HandlerList();
	private Player player;
	private SuicideMethod method;

	public PlayerSuicideEvent(Player player, SuicideMethod method) {
		this.player = player;
		this.method = method;
	}

	public SuicideMethod getMethod() {
		return method;
	}

	public void setMethod(SuicideMethod method) {
		this.method = method;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}

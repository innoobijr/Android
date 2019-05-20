package com.obi.weather.domain.commands

public interface Command<out T> {
    fun execute(): T
}
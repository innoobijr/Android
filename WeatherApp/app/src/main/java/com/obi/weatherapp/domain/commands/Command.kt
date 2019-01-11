package com.obi.weatherapp.domain

public interface Command<out T> {
    fun execute(): T
}
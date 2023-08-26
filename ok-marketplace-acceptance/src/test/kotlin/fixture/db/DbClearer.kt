package ru.otus.otuskotlin.marketplace.blackbox.fixture.db

interface DbClearer {
    /**
     * Очищает БД (возвращает ее к начальному состоянию)
     */
    fun clear()

    fun close()
}
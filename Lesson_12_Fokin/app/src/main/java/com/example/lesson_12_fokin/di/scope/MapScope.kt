package com.example.lesson_12_fokin.di.scope

import javax.inject.Scope

/** Скоуп для модулей и компонента, относящимся к Map фрагменту */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MapScope
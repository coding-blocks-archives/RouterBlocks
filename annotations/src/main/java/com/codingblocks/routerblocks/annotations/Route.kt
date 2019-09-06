package com.codingblocks.routerblocks.annotations

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
@MustBeDocumented
annotation class Route (val value: String)
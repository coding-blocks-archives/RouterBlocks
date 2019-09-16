package com.codingblocks.routerblocks.compiler.generators

import com.codingblocks.routerblocks.annotations.Route
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.Filer
import javax.lang.model.element.Element

object RouterGenerator {

    private fun routerFileBuilder() =
        FileSpec.builder("com.codingblocks.routerblocks", "Router")

    private fun routeHashMapProperty() = PropertySpec.builder(
        "routes",
        HashMap::class.asClassName().parameterizedBy(
            String::class.asClassName(),
            Class::class.asClassName().parameterizedBy(
                WildcardTypeName.producerOf(
                    ClassName("android.app", "Activity")
                )
            )
        )
    ).initializer("hashMapOf<String, Class<out Activity>>()").build()

    private fun addRouteStatement(element: Element, codeblockBuilder: CodeBlock.Builder) =
        codeblockBuilder.addStatement(
            """routes.set("${element.getAnnotation(Route::class.java).value}", %T::class.java)""",
            element.asType()
        )

    private fun routerInitBlock(routeElements: ArrayList<Element>) =
        CodeBlock.builder().apply {
            routeElements.forEach { el -> addRouteStatement(el, this) }
        }.build()

    fun genRouterFile(elements: ArrayList<Element>, filer: Filer) {
        routerFileBuilder()
            .addType(
                TypeSpec.objectBuilder("Router")
                    .addProperty(routeHashMapProperty())
                    .addInitializerBlock(routerInitBlock(elements))
                    .build()
            )
            .build()
            .writeTo(filer)
    }

}
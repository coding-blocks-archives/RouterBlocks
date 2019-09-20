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

    /**
     * Build a `Router.kt` file within the package name provided
     */
    private fun routerFileBuilder(packageName: String = "com.codingblocks.routerblocks") =
        FileSpec.builder(packageName, "Router")

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
        routerFileBuilder(elements[0].takeIf { it.kind.isClass }?.enclosingElement.toString())
            .addType(
                TypeSpec.objectBuilder("Router")
                    .superclass(ClassName("com.codingblocks.routerblocks.router", "BaseRouter"))
                    .addInitializerBlock(routerInitBlock(elements))
                    .build()
            )
            .build()
            .writeTo(filer)
    }

}
package com.codingblocks.routerblocks.compiler

import com.codingblocks.routerblocks.annotations.Route
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class RouteProcessor : AbstractProcessor() {
    private lateinit var filer: Filer
    private lateinit var elementUtils: Elements
    private lateinit var messager: Messager

    override fun init(procEnv: ProcessingEnvironment?) {
        super.init(procEnv)
        procEnv?.let {
            filer = it.filer
            elementUtils = it.elementUtils
            messager = it.messager
        }

    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Route::class.java.name)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        val routeElements = roundEnv?.getElementsAnnotatedWith(Route::class.java) ?: TreeSet<Element>()
        if (routeElements.size < 1) {
            return false
        }

        for (el in routeElements) {
            throw Exception(el.enclosedElements.map { el -> el.simpleName }.toString())
        }

        try {
            FileSpec.builder("com.codingblocks.routerblocks", "Router")
                .addType(TypeSpec.classBuilder("Router").build())
                .build()
                .writeTo(filer)
        } catch (e: Exception) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }


        return true
    }


}

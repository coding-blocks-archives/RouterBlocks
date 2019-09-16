package com.codingblocks.routerblocks.compiler

import com.codingblocks.routerblocks.annotations.Route
import com.codingblocks.routerblocks.compiler.generators.RouterGenerator.genRouterFile
import com.google.auto.service.AutoService
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
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
        val routeElements =
            roundEnv?.getElementsAnnotatedWith(Route::class.java) ?: TreeSet<Element>()
        if (routeElements.size < 1) {
            return false
        }
        val elementList = arrayListOf<Element>().apply { addAll(routeElements) }


        try {
            genRouterFile(elementList, filer)
        } catch (e: Exception) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }


        return true
    }

}

package com.codingblocks.routerblocks.compiler

import com.codingblocks.routerblocks.annotations.Route
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier.PRIVATE
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName
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
import kotlin.collections.HashMap

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


        try {
            FileSpec.builder("com.codingblocks.routerblocks", "Router")
                .addType(
                    TypeSpec.classBuilder("Router")
                        .primaryConstructor(
                            FunSpec.constructorBuilder()
                                .addParameter(
                                    "context",
                                    ClassName("android.content", "Context")
                                )
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder(
                                "context",
                                ClassName("android.content", "Context"),
                                PRIVATE
                            )
                                .initializer("context")
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder(
                                "routes",
                                HashMap::class.asClassName().parameterizedBy(
                                    String::class.asClassName(),
                                    Class::class.asClassName().parameterizedBy(
                                        WildcardTypeName.producerOf(ClassName("android.app", "Activity"))

                                    )
                                )
                            )
                                .initializer("hashMapOf<String, Class<out Activity>>()")
                                .build()
                        )
                        .addFunction(
                            FunSpec.builder("registerRoutes")
                                .addStatement(
                                    """
                                    routes.set("a", %T::class.java)
                                """.trimIndent(), routeElements.iterator().next().asType()
                                )
                                .build()
                        )
                        .build()
                )
                .build()
                .writeTo(filer)
        } catch (e: Exception) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }


        return true
    }

}

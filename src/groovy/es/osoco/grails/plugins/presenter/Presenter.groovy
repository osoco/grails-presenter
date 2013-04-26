package es.osoco.grails.plugins.presenter

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.TYPE])
@GroovyASTTransformationClass(['es.osoco.grails.plugins.presenter.PresenterASTTransformation'])
@interface Presenter {
}
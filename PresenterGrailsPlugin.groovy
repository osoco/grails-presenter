import es.osoco.grails.plugins.presenter.ClassNameAsBeanNameGenerator
import es.osoco.grails.plugins.presenter.Presenter
import es.osoco.grails.plugins.presenter.ScopeAlwaysAsPrototypeResolver
import org.codehaus.groovy.grails.beans.factory.GenericBeanFactoryAccessor
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.ApplicationContext
import org.springframework.core.type.filter.AnnotationTypeFilter

class PresenterGrailsPlugin {
    def version = '0.1'
    def grailsVersion = '1.3.7 > *'
    def title = 'Presenter pattern implementation for Grails'
    def organization = [name: 'OSOCO', url: 'http://osoco.es/']
    def author = 'OSOCO'
    def authorEmail = 'info@osoco.es'
    def developers = [
        [name: 'Arturo García', email: 'arturo.garcia@osoco.es'],
        [name: 'Marcin Gryszko', email: 'marcin.gryszko@osoco.es'],
    ]

    def pluginExcludes = [
        'grails-app/domain/**/*',
        'grails-app/services/**/*',
        'src/groovy/es/osoco/grails/plugins/presenter/test/**/*'
    ]
    def description = '''Presenters decorate domain objects, adapting their interface to what a view requires.\n
They are able to expose domain object properties, transform them and generate HTML markup.'''

    def typeFilters = [new AnnotationTypeFilter(Presenter)]

    def doWithSpring = {
        xmlns grailsContext: "http://grails.org/schema/context"

        def presenterPackages = presenterPackagesFrom(application.config)
        if (presenterPackages) {
            grailsContext.'component-scan'(
                'base-package': presenterPackages.join(','),
                'scope-resolver': ScopeAlwaysAsPrototypeResolver.name,
                'name-generator': ClassNameAsBeanNameGenerator.name
            )
        }
        presenterClassesFrom(application.config).each { presenterClass ->
            "${beanNameFor(presenterClass)}"(presenterClass)  { bean ->
                bean.autowire = 'byName'
                bean.scope = BeanDefinition.SCOPE_PROTOTYPE
            }
        }
    }

    def doWithDynamicMethods = { ctx ->
        application.domainClasses.each { domainClass ->
            domainClass.metaClass.decorate = {
                def presenterClassName = "${domainClass.fullName}Presenter"
                application.classLoader.loadClass(presenterClassName).newInstance()
            }
            domainClass.metaClass.decorate << { Class presenterClass ->
                presenterClass.newInstance()
            }
        }

        allPresenterClasses(application.config, ctx).each { presenterClass ->
            autowireDependenciesOnCreation presenterClass, ctx
        }
    }

    private static allPresenterClasses(config, ctx) {
        def classes = presenterClassesFrom(config).collect { it }
        def accessor = new GenericBeanFactoryAccessor(ctx)
        classes += accessor.getBeansWithAnnotation(Presenter)*.value*.class
    }

    private static presenterPackagesFrom(config) {
        config.grails.presenters.packages
    }

    private static presenterClassesFrom(config) {
        config.grails.presenters.classes
    }

    private static autowireDependenciesOnCreation(presenterClass, ctx) {
        def metaClass = GroovySystem.metaClassRegistry.getMetaClass(presenterClass)
        metaClass.constructor = { ->
            if (ctx.containsBean(beanNameFor(presenterClass))) {
                ctx.getBean beanNameFor(presenterClass)
            }
            else {
                BeanUtils.instantiateClass presenterClass
            }
        }
    }

    private static beanNameFor(presenterClass) {
        presenterClass.name
    }
}

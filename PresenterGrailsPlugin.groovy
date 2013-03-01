import es.osoco.grails.plugins.presenter.Presenter
import es.osoco.grails.plugins.presenter.ScopeAlwaysAsPrototypeResolver
import org.codehaus.groovy.grails.beans.factory.GenericBeanFactoryAccessor
import org.springframework.beans.BeanUtils
import org.springframework.context.ApplicationContext
import org.springframework.core.type.filter.AnnotationTypeFilter

class PresenterGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.9 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Your name"
    def authorEmail = ""
    def title = "Plugin summary/headline"
    def description = 'Brief description of the plugin'

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/presenter"

    def typeFilters = [new AnnotationTypeFilter(Presenter)]

    def doWithSpring = {
        xmlns grailsContext:"http://grails.org/schema/context"

        def packagesToScan = [] + application.config.grails.presenters.packages
        if (packagesToScan) {
            grailsContext.'component-scan'(
                'base-package': packagesToScan.join(','),
                'scope-resolver': ScopeAlwaysAsPrototypeResolver.name
            )
        }
    }

    def doWithDynamicMethods = { ctx ->
        for (domainClass in application.domainClasses) {
            domainClass.metaClass.decorate = {
            }
        }

        def accessor = new GenericBeanFactoryAccessor(ctx)
        accessor.getBeansWithAnnotation(Presenter).each { name, presenter ->
            enhancePresenter presenter.class, name, ctx
        }
    }

    private static enhancePresenter(Class clazz, String name, ApplicationContext ctx) {
        def metaClass = GroovySystem.metaClassRegistry.getMetaClass(clazz)
        metaClass.constructor = { ->
            if (ctx.containsBean(name)) {
                ctx.getBean name
            }
            else {
                BeanUtils.instantiateClass clazz
            }
        }
    }
}

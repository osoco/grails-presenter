import es.osoco.grails.plugins.presenter.test.manualconfig.ManuallyConfiguredPresenter

grails.presenters.packages = ['es.osoco.grails.plugins.presenter.test.autoscan']
grails.presenters.classes = [ManuallyConfiguredPresenter]

log4j = {
    error 'org.codehaus.groovy.grails',
        'org.springframework',
        'org.hibernate',
        'net.sf.ehcache.hibernate'
}

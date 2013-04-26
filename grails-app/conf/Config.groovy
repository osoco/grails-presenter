import es.osoco.grails.plugins.presenter.test.manualconfig.AlternativeAuthorPresenter

grails.presenters.packages = ['es.osoco.grails.plugins.presenter.test.autoscan']
grails.presenters.classes = [AlternativeAuthorPresenter]

log4j = {
    error 'org.codehaus.groovy.grails',
        'org.springframework',
        'org.hibernate',
        'net.sf.ehcache.hibernate'
}

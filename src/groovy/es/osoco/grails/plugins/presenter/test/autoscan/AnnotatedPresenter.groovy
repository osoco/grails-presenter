package es.osoco.grails.plugins.presenter.test.autoscan

import es.osoco.grails.plugins.presenter.Presenter
import org.springframework.beans.factory.annotation.Autowired

@Presenter
class AnnotatedPresenter {
    @Autowired
    def weirdService
}

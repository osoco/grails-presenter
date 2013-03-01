package es.osoco.grails.plugins.presenter.test

import es.osoco.grails.plugins.presenter.Presenter
import org.springframework.beans.factory.annotation.Autowired

@Presenter
class AuthorPresenter {
    @Autowired
    def weirdService
}

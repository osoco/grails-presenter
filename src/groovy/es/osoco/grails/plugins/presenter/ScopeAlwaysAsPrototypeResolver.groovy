package es.osoco.grails.plugins.presenter

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.ScopeMetadata
import org.springframework.context.annotation.ScopeMetadataResolver

class ScopeAlwaysAsPrototypeResolver implements ScopeMetadataResolver {
    ScopeMetadata resolveScopeMetadata(BeanDefinition beanDefinition) {
        new ScopeMetadata(scopeName: BeanDefinition.SCOPE_PROTOTYPE)
    }
}

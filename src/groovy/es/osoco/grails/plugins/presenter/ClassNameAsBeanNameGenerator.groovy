package es.osoco.grails.plugins.presenter

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanNameGenerator

class ClassNameAsBeanNameGenerator implements BeanNameGenerator {
    String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry beanDefinitionRegistry) {
        beanDefinition.beanClassName
    }
}

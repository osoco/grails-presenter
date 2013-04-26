package es.osoco.grails.plugins.presenter

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.messages.SyntaxErrorMessage
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class PresenterASTTransformation implements ASTTransformation {

    private static final OBJECT = new ClassNode(Object)
    private static final STRING = new ClassNode(String)
    private static final NO_EXCEPTIONS = [] as ClassNode[]
    private static final NO_VARIABLE_SCOPE = null

    void visit(ASTNode[] nodes, SourceUnit source) {
        checkIfTransformationApplicable(nodes, source)

        ClassNode classNode = nodes[1]
        addDestinationFieldTo classNode
        addDelegatingPropertyMissingTo classNode
        addDelegatingMethodMissingTo classNode
    }

    private void checkIfTransformationApplicable(ASTNode[] nodes, SourceUnit source) {
        if (nodes.length != 2 || !(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
            def syntaxEx = new SyntaxException(
                "Internal error: expecting [AnnotationNode, AnnotatedNode] but got: ${nodes.toList()}\n",
                nodes[0].getLineNumber(), nodes[0].getColumnNumber()
            )
            source.getErrorCollector().addError new SyntaxErrorMessage(syntaxEx, source)
        }
    }

    private addDestinationFieldTo(classNode) {
        classNode.addField('destination', classNode.ACC_PUBLIC, OBJECT, null)
    }

    private addDelegatingPropertyMissingTo(ClassNode classNode) {
        def methodBody = new BlockStatement([
            new ExpressionStatement(
                new MethodCallExpression(
                    new VariableExpression('destination'),
                    'getProperty',
                    new ArgumentListExpression(new VariableExpression('name', STRING))
                )
            )
        ], NO_VARIABLE_SCOPE)

        classNode.addMethod(
            new MethodNode(
                'propertyMissing',
                classNode.ACC_PUBLIC,
                OBJECT,
                [new Parameter(STRING, 'name')] as Parameter[],
                NO_EXCEPTIONS,
                methodBody
            )
        )
    }

    private addDelegatingMethodMissingTo(ClassNode classNode) {
        def methodBody = new BlockStatement([
            new ExpressionStatement(
                new MethodCallExpression(
                    new VariableExpression('destination'),
                    'invokeMethod',
                    new ArgumentListExpression(
                        new VariableExpression('name', STRING), new VariableExpression('args', OBJECT)
                    )
                )
            )
        ], NO_VARIABLE_SCOPE)

        classNode.addMethod(
            new MethodNode(
                'methodMissing',
                classNode.ACC_PUBLIC,
                OBJECT,
                [new Parameter(STRING, 'name'), new Parameter(OBJECT, 'args')] as Parameter[],
                NO_EXCEPTIONS,
                methodBody
            )
        )
    }
}

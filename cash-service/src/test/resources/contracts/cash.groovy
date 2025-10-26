package contracts
import org.springframework.cloud.contract.spec.Contract
[
        Contract.make {
            description "POST /{login}/withdraw с amount и currency возвращает 200 и 'Success'"
            request {
                method 'POST'
                urlPath(
                        $(consumer(regex('/[A-Za-z0-9._-]{3,32}/withdraw')),
                                producer('/john-doe/withdraw'))
                )
              body(
                    amount: $(consumer(regex('-?\\d+(\\.\\d+)?')), producer('250.00')),
                    currency: $(consumer(regex('[A-Z]{3}')), producer('USD'))
              )
                bodyMatchers {
                    jsonPath('$.amount', byRegex(number()))   // число, не строка
                    jsonPath('$.currency', byRegex('[A-Z]{3}'))
                }
                headers {
                    contentType(applicationJson())
                    accept(textPlain())
                }
            }
            response {
                status 200
                body('Success')
                headers {
                    contentType(textPlain())
                }
            }
        },
        Contract.make {
            description "POST /{login}/deposit с amount и currency возвращает 200 и 'Success'"
            request {
                method 'POST'
                urlPath(
                        $(consumer(regex('/[A-Za-z0-9._-]{3,32}/deposit')),
                                producer('/john-doe/deposit'))
                )
                body(
                    amount: $(consumer(regex('-?\\d+(\\.\\d+)?')), producer('250.00')),
                    currency: $(consumer(regex('[A-Z]{3}')), producer('USD'))
                )
                bodyMatchers {
                    jsonPath('$.amount', byRegex(number()))   // число, не строка
                    jsonPath('$.currency', byRegex('[A-Z]{3}'))
                }
                headers {
                    contentType(applicationJson())
                    accept(textPlain())
                }
            }
            response {
                status 200
                body('Success')
                headers {
                    contentType(textPlain())
                }
            }
        }
]
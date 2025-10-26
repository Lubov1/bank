package contracts
import org.springframework.cloud.contract.spec.Contract
[
        Contract.make {
            description "POST /{login}/transfer с amount и currency возвращает 200"
            request {
                method 'POST'
                urlPath(
                        $(consumer(regex('/[A-Za-z0-9._-]{3,32}/transfer')),
                                producer('/johndoe/transfer'))
                )
              body(
                    amount: $(consumer(regex('-?\\d+(\\.\\d+)?')), producer('250.00')),
                    currencyFrom: $(consumer(regex('[A-Z]{3}')), producer('USD')),
                    currencyTo: $(consumer(regex('[A-Z]{3}')), producer('USD')),
                    loginTo: $(consumer(regex('[A-Za-z0-9._-]{3,32}')), producer('johndoe'))
              )
                bodyMatchers {
                    jsonPath('$.amount', byRegex(number()))
                    jsonPath('$.currencyFrom', byRegex('[A-Z]{3}'))
                    jsonPath('$.currencyTo', byRegex('[A-Z]{3}'))
                    jsonPath('$.loginTo', byRegex('[A-Za-z0-9._-]{3,32}'))
                }
                headers {
                    contentType(applicationJson())
                }
            }
            response {
                status 200
            }
        }
]
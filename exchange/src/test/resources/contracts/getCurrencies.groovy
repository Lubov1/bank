package contracts
import org.springframework.cloud.contract.spec.Contract
[
    Contract.make {
        description "POST /getCurrencies возвращает курсы"
        request {
            method 'POST'
            url '/getCurrencies'
            body(
                    USD: $(regex(number())),
                    RUB: $(regex(number())),
                    CNY: $(regex(number()))
            )
            headers {
                contentType(applicationJson())
                accept(applicationJson())
            }
        }
        response {
            status 200
        }
    },

    Contract.make {
        request {
            method 'GET'
            url '/getCurrencies'
            headers {
                contentType(applicationJson())
                accept(applicationJson())
            }

        }
        response {
            status 200
            body(
                    USD: [title: 'USD', name: 'USD', value: $(regex(number()))],
                    RUB: [title: 'RUB', name: 'RUB', value: $(regex(number()))],
                    CNY: [title: 'CNY', name: 'CNY', value: $(regex(number()))]
            )
            headers {
                contentType(applicationJson())
            }
        }
    }
]
package com.kath.cineapp.data.remote.model

data class PayURequest(
    val language: String = "es",
    val command: String = "SUBMIT_TRANSACTION",
    val merchant: Merchant = Merchant(),
    val transaction: Transaction = Transaction(),
    val test: Boolean = true
)

data class Merchant(
    val apiKey : String = "4Vj8eK4rloUd272L48hsrarnUA",
    val apiLogin: String = "pRRXKOl8ikMmt9u"
)

data class Transaction(
    val order : Order = Order(),
    val payer: Payer = Payer(),
    val creditCard: CreditCard = CreditCard(),
    val extraParameters: ExtraParameters = ExtraParameters(),
    val type: String = "AUTHORIZATION_AND_CAPTURE",
    val paymentMethod: String = "VISA",
    val paymentCountry: String = "PE",
    val deviceSessionId: String = "vghs6tvkcle931686k1900o6e1",
    val ipAddress: String = "127.0.0.1",
    val cookie: String = "pt1t38347bs6jc9ruv2ecpv7o2",
    val userAgent: String = "Mozilla/5.0 (Windows NT 5.1; rv:18.0) Gecko/20100101 Firefox/18.0"
)

data class Order(
    val accountId : String = "512323",
    val referenceCode: String = "PRODUCT_TEST",
    val description: String = "Payment test description",
    val language: String = "es",
    val signature: String = "6421c6e162316482d0b5c3e10ae9ce88",
    val notifyUrl : String = "http://www.payu.com/notify",
    val additionalValues: AdditionalValue = AdditionalValue(TxValues()),
    val buyer: Buyer = Buyer(shippingAddress = Address()),
    val shippingAddress: Address = Address()
)

data class AdditionalValue(
    val TX_VALUE: TxValues
)

data class TxValues(
    val value : Double = 100.0,
    val currency: String = "PEN"
)

data class Buyer(
    val merchantBuyerId: String = "1",
    val fullName: String = "First name and second buyer name",
    val emailAddress: String = "buyer_test@test.com",
    val contactPhone: String = "7563126",
    val dniNumber: String = "123456789",
    val shippingAddress: Address
)

data class Address(
    val street1: String = "Av. Isabel La Católica 103-La Victoria",
    val street2: String = "5555487",
    val city: String = "Lima",
    val state: String = "Lima y Callao",
    val country: String = "PE",
    val postalCode: String = "000000",
    val phone: String = "7563126"
)

data class Payer(
    val merchantPayerId: String = "1",
    val fullName: String = "First name and second buyer name",
    val emailAddress: String = "buyer_test@test.com",
    val contactPhone: String = "7563126",
    val dniNumber: String = "123456789",
    val billingAddress: Address = Address()
)

data class CreditCard(
    val number: String = "4097440000000004",
    val securityCode: String = "777",
    val expirationDate: String = "2025/06",
    val name: String = "APPROVED"
)

data class ExtraParameters(
    val INSTALLMENTS_NUMBER: Int = 1
)

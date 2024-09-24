package fastcampus.ecommerce.api.domain.payment;


public enum PaymentMethod {
  CREDIT_CARD("신용 카드"),
  DEBIT_CARD("직불 카드"),
  PAYPAL("페이팔"),
  BANK_TRANSFER("계좌 이체");

  final String desc;

  PaymentMethod(String desc) {
    this.desc = desc;
  }
}
import doxflow.diagram_8x_flow
import doxflow.dsl.fulfillment
import doxflow.models.diagram.Relationship.Companion.ONE_TO_N

diagram_8x_flow {
    lateinit var payment: fulfillment
    lateinit var payment2: fulfillment
    lateinit var fapiao: fulfillment
    lateinit var fapiao2: fulfillment

    context("求租委托协议上下文") {
        val platformUserOne = participant_party("平台用户")
        val platformUserTwo = participant_party("平台用户")

        val platform = participant_place("平台")

        val renter = role_party("租客")
        val agent = role_party("中介代理人")

        platformUserOne play renter
        platformUserTwo play agent

        rfp("租客查看房源", renter, ONE_TO_N) {
            key_timestamps("创建时间")

            platform.relate(this)

            proposal("中介代理人与租客协商租赁方案", agent, ONE_TO_N) {
                key_timestamps("创建时间")
                key_data("房租", "租期", "付款方式")

                platform.relate(this)

                contract("求租委托协议", renter, agent) {
                    key_timestamps("签订时间")
                    key_data("协议开始时间", "协议结束时间")

                    fulfillment("与房东签订租赁合同", ONE_TO_N) {
                        request(agent) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(renter) {
                            key_timestamps("创建时间")

                            evidence("与房东签订租赁合同凭证") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                    payment = fulfillment("支付佣金", ONE_TO_N) {
                        request(agent) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(renter) {
                            key_timestamps("创建时间")

                            evidence("佣金支付凭证") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                    fapiao = fulfillment("提供佣金支付发票", ONE_TO_N) {
                        request(renter) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(agent) {
                            key_timestamps("创建时间")

                            evidence("提供佣金支付发票凭证") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                    payment2 = fulfillment("违约金支付", ONE_TO_N) {
                        request(agent) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(renter) {
                            key_timestamps("创建时间")

                            evidence("违约金支付凭证") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                    fapiao2 = fulfillment("违约金支付发票", ONE_TO_N) {
                        request(renter) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(agent) {
                            key_timestamps("创建时间")

                            evidence("违约金支付发票凭证") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                }

            }
        }
    }

    context("微信支付平台上下文") {
        contract("微信支付协议") {
            key_timestamps("创建时间")

            fulfillment("微信支付") {
                request {
                    key_timestamps("创建时间", "过期时间")
                }

                confirmation {
                    key_timestamps("创建时间")

                    this play payment.confirmation
                }
            }
        }
    }

    context("支付宝支付平台上下文") {
        contract("支付宝支付协议") {
            key_timestamps("创建时间")

            fulfillment("支付宝支付") {
                request {
                    key_timestamps("创建时间", "过期时间")
                }

                confirmation {
                    key_timestamps("创建时间")

                    this play payment.confirmation
                    this play payment2.confirmation
                }
            }
        }
    }

    context("发票平台上下文") {
        contract("发票平台协议") {
            key_timestamps("创建时间")

            fulfillment("发票开具") {
                request {
                    key_timestamps("创建时间", "过期时间")
                }

                confirmation {
                    key_timestamps("创建时间")

                    this play fapiao.confirmation
                    this play fapiao2.confirmation
                }
            }
        }
    }

} export "./../diagrams/training/求租委托协议上下文.png"
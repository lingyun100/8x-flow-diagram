import doxflow.diagram_8x_flow
import doxflow.dsl.fulfillment
import doxflow.models.diagram.Relationship.Companion.ONE_TO_N

diagram_8x_flow {
    lateinit var payment: fulfillment
    lateinit var fapiao: fulfillment

    context("租赁合同上下文") {
        val platformUserOne = participant_party("平台用户")
        val platformUserTwo = participant_party("平台用户")
        val platformUserThree = participant_party("平台用户")

        val platform = participant_place("平台")

        val houseOwner = role_party("房东")
        val renter = role_party("租客")
        val agent = role_party("中介代理人")

        platformUserOne play houseOwner
        platformUserTwo play agent
        platformUserThree play renter

        rfp("代理人撮合房东和租客签订租赁合同", agent, ONE_TO_N) {
            key_timestamps("创建时间")

            platform.relate(this)

            proposal("代理人协调房东和租客在线完成租赁合同签约", agent, ONE_TO_N) {
                key_timestamps("创建时间")
                key_data("价格", "租期", "房屋地址", "租赁用途", "房屋附属配件", "租金", "租金支付方式")

                platform.relate(this)

                contract("租赁合同", houseOwner, agent, renter) {
                    key_timestamps("签订时间")
                    key_data("合同开始时间", "合同结束时间")

                    fulfillment("房东签约租赁合同", ONE_TO_N) {
                        request(agent) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(houseOwner) {
                            key_timestamps("创建时间")

                            evidence("房东签约租赁合同凭证") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                    fulfillment("租客签约租赁合同", ONE_TO_N) {
                        request(agent) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(renter) {
                            key_timestamps("创建时间")

                            evidence("租客签约租赁合同凭证") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                    payment = fulfillment("租客向房东支付第一笔房租", ONE_TO_N) {
                        request(houseOwner) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(renter) {
                            key_timestamps("创建时间")

                            evidence("租客向房东支付第一笔房租凭证") {
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
                }
            }
        }
    }

} export "./../diagrams/training/租赁合同上下文.png"
import doxflow.diagram_8x_flow
import doxflow.dsl.fulfillment
import doxflow.models.diagram.Relationship.Companion.ONE_TO_N

diagram_8x_flow {
    lateinit var payment: fulfillment
    lateinit var fapiao: fulfillment

    context("租赁委托协议上下文") {
        val platformUserOne = participant_party("平台用户")
        val platformUserTwo = participant_party("平台用户")

        val platform = participant_place("平台")

        val houseOwner = role_party("房东")
        val agent = role_party("中介代理人")

        platformUserOne play houseOwner
        platformUserTwo play agent

        rfp("登记租赁意向", houseOwner, ONE_TO_N) {
            key_timestamps("创建时间")
            key_data("房屋户型", "面积", "地址", "联系方式", "预期房租范围", "出租时间", "开始时间")

            platform.relate(this)

            proposal("商讨租赁委托方案", agent, ONE_TO_N) {
                key_timestamps("创建时间")
                key_data("租金", "租赁委托要求", "出租时间")

                platform.relate(this)

                contract("租赁委托协议", houseOwner, agent) {
                    key_timestamps("签订时间")
                    key_data("协议开始时间", "协议结束时间")

                    fulfillment("提供钥匙或开门方式", ONE_TO_N) {
                        request(agent) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(houseOwner) {
                            key_timestamps("创建时间")

                            evidence("提供钥匙或开门方式交付凭证") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                    fulfillment("发布房屋信息", ONE_TO_N) {
                        request(houseOwner) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(agent) {
                            key_timestamps("创建时间")

                            evidence("房屋信息发布") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                    fulfillment("寻找符合要求的租客", ONE_TO_N) {
                        request(houseOwner) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(agent) {
                            key_timestamps("创建时间")

                            evidence("合约失效凭证") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                    fulfillment("与租客签订租赁合同", ONE_TO_N) {
                        request(agent) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(houseOwner) {
                            key_timestamps("创建时间")

                            evidence("与租客签订租赁合同凭证") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                    fulfillment("将租房合约或附件作为额外凭据录入系统", ONE_TO_N) {
                        request(houseOwner) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(agent) {
                            key_timestamps("创建时间")

                            evidence("租房合约或附件作为额外凭据录入系统凭证") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                    payment = fulfillment("支付佣金", ONE_TO_N) {
                        request(agent) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(houseOwner) {
                            key_timestamps("创建时间")

                            evidence("佣金支付凭证") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                    fapiao = fulfillment("提供佣金支付发票", ONE_TO_N) {
                        request(houseOwner) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(agent) {
                            key_timestamps("创建时间")

                            evidence("提供佣金支付发票凭证") {
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
                }
            }
        }
    }

} export "./../diagrams/training/租赁委托协议上下文.png"
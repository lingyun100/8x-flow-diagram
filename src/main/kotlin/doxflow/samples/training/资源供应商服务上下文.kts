package doxflow.samples.training

import doxflow.diagram_8x_flow
import doxflow.dsl.fulfillment
import doxflow.models.diagram.Relationship.Companion.ONE_TO_N

diagram_8x_flow {
    lateinit var importResource: fulfillment
    lateinit var deleteResource: fulfillment
    lateinit var updateResource: fulfillment
    lateinit var onlinePaymentAll: fulfillment
    lateinit var outdatedPaymentFulfillment: fulfillment

    context("资源供应商服务上下文") {
        val learningPlatformUser = participant_party("学之道用户")
        val techCompany = participant_party("科技公司下属的在线教育平台")

        val resourceProvider = role_party("资源供应商")
        val platform = role_party("学之道")

        learningPlatformUser play resourceProvider
        techCompany play platform

        rfp("发布资源竞标邀约", platform, ONE_TO_N) {
            key_timestamps("创建时间")
            key_data("教育资源需求")

            proposal("提交提案", resourceProvider, ONE_TO_N) {
                key_timestamps("创建时间")
                key_data("需求响应")

                contract("资源供应商合同", resourceProvider, platform) {
                    key_timestamps("签订时间")
                    key_data("合同开始时间", "合同结束时间")

                    importResource = fulfillment("导入资源", ONE_TO_N) {
                        request(resourceProvider) {
                            key_timestamps("创建时间")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")

                            evidence("资源导入记录") {
                                key_timestamps("导入开始时间", "导入完成时间")
                            }
                        }
                    }

                    deleteResource = fulfillment("删除资源", ONE_TO_N) {
                        request(resourceProvider) {
                            key_timestamps("创建时间")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")

                            evidence("资源删除记录") {
                                key_timestamps("删除开始时间", "删除结束时间")
                            }
                        }
                    }

                    updateResource = fulfillment("更新资源", ONE_TO_N) {
                        request(resourceProvider) {
                            key_timestamps("创建时间")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")

                            evidence("资源更新记录") {
                                key_timestamps("更新开始时间", "更新结束时间")
                            }
                        }
                    }

                    fulfillment("授予对资源的使用 传播 售卖等权利", ONE_TO_N) {
                        request(resourceProvider) {
                            key_timestamps("创建时间", "过期时间")
                            key_data("有效的资源版权凭证")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")

                            evidence("有效的资源版权凭证") {
                                key_timestamps("生成时间")
                                key_data("凭证详情")
                            }
                        }
                    }

                    fulfillment("提供符合税务规定的有效发票", ONE_TO_N) {
                        request(resourceProvider) {
                            key_timestamps("创建时间", "过期时间")
                            key_data("有效发票")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")

                            evidence("有效发票") {
                                key_timestamps("生成时间")
                                key_data("发票详情")
                            }

                        }
                    }

                    onlinePaymentAll = fulfillment("在线方式支付全额款项", ONE_TO_N) {
                        request(resourceProvider) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")
                        }
                    }

                    fulfillment("反馈资源质量", ONE_TO_N) {
                        request(platform) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(resourceProvider) {
                            key_timestamps("创建时间")

                            evidence("资源整改记录") {
                                key_timestamps("删除或更新时间")
                            }
                        }
                    }

                    outdatedPaymentFulfillment = fulfillment("违约金支付", ONE_TO_N) {
                        request(platform) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(resourceProvider) {
                            key_timestamps("创建时间")
                        }
                    }
                }

            }
        }
    }

    context("课程内容平台上下文") {
        contract("课程内容平台服务协议") {
            key_timestamps("创建时间")

            fulfillment("存储教学资源", ONE_TO_N) {
                request {
                    key_timestamps("创建时间")
                }

                confirmation {
                    key_timestamps("创建时间")

                    this play importResource.confirmation
                }
            }

            fulfillment("更新教学资源", ONE_TO_N) {
                request {
                    key_timestamps("创建时间")
                }

                confirmation {
                    key_timestamps("创建时间")

                    this play updateResource.confirmation
                }
            }

            fulfillment("删除教学资源", ONE_TO_N) {
                request {
                    key_timestamps("创建时间")
                }

                confirmation {
                    key_timestamps("创建时间")

                    this play deleteResource.confirmation
                }
            }
        }
    }

    context("支付平台上下文") {
        contract("支付平台协议") {
            key_timestamps("创建时间")

            fulfillment("支付", ONE_TO_N) {
                request {
                    key_timestamps("创建时间", "过期时间")
                }

                confirmation {
                    key_timestamps("创建时间")

                    this play onlinePaymentAll.confirmation
                    this play outdatedPaymentFulfillment.confirmation
                }
            }
        }
    }

} export "./diagrams/training/资源供应商服务上下文.png"
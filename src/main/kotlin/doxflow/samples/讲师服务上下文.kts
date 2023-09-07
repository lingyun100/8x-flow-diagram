package doxflow.samples

import doxflow.diagram_8x_flow
import doxflow.dsl.fulfillment
import doxflow.models.diagram.Relationship.Companion.ONE_TO_N

diagram_8x_flow {
    lateinit var normalTeachFulfillment: fulfillment
    lateinit var customerizeTeachFulfillment: fulfillment
    lateinit var overduePaymentFulfillment: fulfillment
    lateinit var outdatedPaymentFulfillment: fulfillment

    lateinit var saveCourse: fulfillment

    context("讲师服务上下文") {
        val learningPlatformUser = participant_party("学之道用户")
        val techCompany = participant_party("科技公司下属的在线教育平台")

        val teacher = role_party("讲师")
        val platform = role_party("学之道")

        learningPlatformUser play teacher
        techCompany play platform

        rfp("发布课程竞标邀约", platform, ONE_TO_N) {
            key_timestamps("创建时间")
            key_data("讲师服务需求")


            proposal("提交提案", teacher, ONE_TO_N) {
                key_timestamps("创建时间")
                key_data("需求响应")


                contract("讲师服务合同", teacher, platform) {
                    key_timestamps("签订时间")
                    key_data("合同开始时间", "合同结束时间")

                    saveCourse = fulfillment("直播或录播课程", ONE_TO_N) {
                        request(teacher) {
                            key_timestamps("创建时间")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")

                            evidence("直播或录播课程记录") {
                                key_timestamps("开始时间", "结束时间")
                            }
                        }
                    }

                    fulfillment("个性化辅导服务", ONE_TO_N) {
                        request(teacher) {
                            key_timestamps("创建时间")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")

                            evidence("个性化辅导服务记录") {
                                key_timestamps("开始时间", "结束时间")
                            }
                        }
                    }

                    normalTeachFulfillment = fulfillment("支付课程报酬", ONE_TO_N) {
                        request(platform) {
                            key_timestamps("创建时间", "过期时间")
                            key_data("收款账号")
                        }

                        confirmation(teacher) {
                            key_timestamps("创建时间")
                        }
                    }

                    customerizeTeachFulfillment = fulfillment("支付个性化辅导费用", ONE_TO_N) {
                        request(platform) {
                            key_timestamps("创建时间", "过期时间")
                            key_data("收款账号")
                        }

                        confirmation(teacher) {
                            key_timestamps("创建时间")
                        }
                    }

                    overduePaymentFulfillment = fulfillment("滞纳金支付", ONE_TO_N) {
                        request(teacher) {
                            key_timestamps("创建时间", "过期时间")
                            key_data("收款账号")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")
                        }
                    }

                    outdatedPaymentFulfillment = fulfillment("违约金支付", ONE_TO_N) {
                        request(platform) {
                            key_timestamps("创建时间", "过期时间")
                            key_data("收款账号")
                        }

                        confirmation(teacher) {
                            key_timestamps("创建时间")
                        }
                    }
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

                    this play normalTeachFulfillment.confirmation
                    this play customerizeTeachFulfillment.confirmation
                    this play overduePaymentFulfillment.confirmation
                    this play outdatedPaymentFulfillment.confirmation
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

                    this play saveCourse.confirmation
                }
            }
        }
    }

} export "./diagrams/讲师服务上下文.png"
package doxflow.samples

import doxflow.diagram_8x_flow
import doxflow.dsl.fulfillment
import doxflow.models.diagram.Relationship
import doxflow.models.diagram.Relationship.Companion.ONE_TO_N

diagram_8x_flow {
    context("讲师服务上下文") {
        val learningPlatformUser = participant_party("学之道用户")
        val techCompany = participant_party("科技公司下属的在线教育平台")


        val teacher = role_party("讲师")
        val platform = role_party("学之道")

        learningPlatformUser play teacher
        techCompany play platform

        rfp("课程竞标邀约", platform, ONE_TO_N) {
            key_timestamps("创建时间")
            key_data("讲师服务需求")
            resource = "course-offer"

            proposal("提交提案", teacher, ONE_TO_N) {
                key_timestamps("创建时间")
                key_data("需求响应")


                contract("讲师服务合同", teacher, platform) {
                    key_timestamps("签订时间")
                    key_data("合同开始时间", "合同结束时间")

                    resource = "lecturer-service"
                    relationship = ONE_TO_N

                    fulfillment("直播或录播课程", ONE_TO_N) {
                        resource = "live-course"
                        request(platform) {
                            key_timestamps("创建时间")
                        }

                        confirmation(teacher) {
                            key_timestamps("创建时间")

                            evidence("直播或录播课程记录") {
                                key_timestamps("开始时间", "结束时间")
                            }
                        }
                    }

                    fulfillment("个性化辅导服务", ONE_TO_N) {
                        resource = "tutor"
                        request(platform) {
                            key_timestamps("创建时间")
                        }

                        confirmation(teacher) {
                            key_timestamps("创建时间")

                            evidence("个性化辅导服务记录") {
                                key_timestamps("开始时间", "结束时间")
                            }
                        }
                    }

                     fulfillment("课程转储", ONE_TO_N) {
                        resource = "course-dump"
                        request(platform) {
                            key_timestamps("创建时间")
                            key_data("课程记录")
                        }

                        confirmation {
                            key_timestamps("创建时间")
                        }
                    }

                     fulfillment("支付课程报酬", ONE_TO_N) {
                        resource = "course-payment"
                        request(teacher) {
                            key_timestamps("创建时间", "过期时间")
                            key_data("收款账号")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")
                        }
                    }

                     fulfillment("支付个性化辅导费用", ONE_TO_N) {
                        resource = "tutor-payment"
                        request(teacher) {
                            key_timestamps("创建时间", "过期时间")
                            key_data("收款账号")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")
                        }
                    }

                     fulfillment("滞纳金支付", ONE_TO_N) {
                        resource = "late-fees-payment"
                        request(teacher) {
                            key_timestamps("创建时间", "过期时间")
                            key_data("收款账号")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")
                        }
                    }

                     fulfillment("违约金支付", ONE_TO_N) {
                        resource = "liquidated-payment"
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

//    context("支付平台上下文") {
//        contract("支付平台协议") {
//            key_timestamps("创建时间")
//
//            fulfillment("支付", ONE_TO_N) {
//                request {
//                    key_timestamps("创建时间", "过期时间")
//                }
//
//                confirmation {
//                    key_timestamps("创建时间")
//
//                    this play normalTeachFulfillment.confirmation
//                    this play customerizeTeachFulfillment.confirmation
//                    this play overduePaymentFulfillment.confirmation
//                    this play outdatedPaymentFulfillment.confirmation
//                }
//            }
//        }
//    }

//    context("课程内容平台上下文") {
//        contract("课程内容平台服务协议") {
//            key_timestamps("创建时间")
//
//            fulfillment("存储教学资源", ONE_TO_N) {
//                request {
//                    key_timestamps("创建时间")
//                }
//
//                confirmation {
//                    key_timestamps("创建时间")
//
//                    this play saveCourse.confirmation
//                }
//            }
//        }
//    }

} export_doc "./docs/讲师服务上下文.md"
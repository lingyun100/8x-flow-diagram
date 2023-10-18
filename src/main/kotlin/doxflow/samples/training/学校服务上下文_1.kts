package doxflow.samples.training

import doxflow.diagram_8x_flow
import doxflow.dsl.fulfillment
import doxflow.models.diagram.Relationship.Companion.ONE_TO_N

diagram_8x_flow {
//    lateinit var payFixFee: fulfillment
//    lateinit var grantAccess: fulfillment
//    lateinit var viewCourses: fulfillment
//
//    lateinit var provideServiceData: fulfillment
//
//    lateinit var overduePaymentFulfillment: fulfillment
//    lateinit var outdatedPaymentFulfillment: fulfillment

    context("学校服务上下文") {
        val school = participant_party("学校")
        val techCompany = participant_party("科技公司下属的在线教育平台")
        val student = participant_party("学之道用户")
        val someTeacher = participant_party("学之道用户")
        val someResourceProvider = participant_party("学之道用户")

        val learningPlatform = participant_place("线下")

        val courseBuyer = role_party("课程采购者")
        val platform = role_party("学之道")
        val courseUser = role_party("课程使用者")
        val teacher = role_party("讲师")
        val resourceProvider = role_party("资源供应商")


        school play courseBuyer
        techCompany play platform
        student play courseUser
        someTeacher play teacher
        someResourceProvider play resourceProvider

        rfp("采购课程", courseBuyer, ONE_TO_N) {
            key_timestamps("创建时间")
            key_data("课程领域类型", "课程级别", "合同生效时限")

            resource = "purchasing-course"

            learningPlatform.relate(this)

            proposal("提交提案", platform, ONE_TO_N) {
                key_timestamps("创建时间")
                key_data("课程领域类型", "课程级别", "产品和服务定价")

                learningPlatform.relate(this)

                contract("学校服务合同", courseBuyer, platform, courseUser, teacher, resourceProvider) {
                    key_timestamps("签订时间")
                    key_data("合同开始时间", "合同结束时间")

                    resource = "school-service"
                    relationship = ONE_TO_N

                     fulfillment("支付固定费用", ONE_TO_N) {
                         resource = "fixed-fee-payment"
                        request(platform) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(courseBuyer) {
                            key_timestamps("创建时间")

                        }
                    }

                     fulfillment("开通学生学之道使用权限", ONE_TO_N) {
                         resource = "open-permission"
                        request(courseBuyer) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")
                        }
                    }

                     fulfillment("查看课程", ONE_TO_N) {
                         resource = "view-course"
                        request(courseUser) {
                            key_timestamps("创建时间")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")
                        }
                    }

                    fulfillment("提供技术支持", ONE_TO_N) {
                        resource = "tech-support"
                        request(courseBuyer) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")

                            evidence("提供培训或升级系统等记录") {
                                key_timestamps("创建时间")
                            }
                        }
                    }

                    fulfillment("提出改进", ONE_TO_N) {
                        resource = "improvement"
                        request(courseBuyer) {
                            key_timestamps("创建时间", "过期时间")

                            key_data("改进意见")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")

                            evidence("整改完成记录") {
                                key_timestamps("创建时间")
                            }

                        }
                    }

                     fulfillment("提供服务使用相关数据", ONE_TO_N) {
                         resource = "service-data"
                        request(courseBuyer) {
                            key_timestamps("创建时间", "过期时间")

                            key_data("视频播放量", "完播率")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")
                        }
                    }

                     fulfillment("滞纳金支付", ONE_TO_N) {
                         resource = "late-fee-payment"
                        request(courseBuyer) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(platform) {
                            key_timestamps("创建时间")
                        }
                    }

                     fulfillment("违约金支付", ONE_TO_N) {
                         resource = "liquidated-payment"
                        request(platform) {
                            key_timestamps("创建时间", "过期时间")
                        }

                        confirmation(courseBuyer) {
                            key_timestamps("创建时间")
                        }
                    }
                }

            }
        }
    }

//    context("支付平台上下文") {
//        contract("微信支付协议") {
//            key_timestamps("创建时间")
//
//            fulfillment("微信支付") {
//                request {
//                    key_timestamps("创建时间", "过期时间")
//                }
//
//                confirmation {
//                    key_timestamps("创建时间")
//
//                    this play payFixFee.confirmation
//                    this play overduePaymentFulfillment.confirmation
//                    this play outdatedPaymentFulfillment.confirmation
//                }
//            }
//        }
//    }
//
//    context("账户管理系统上下文") {
//        contract("账户管理系统服务协议") {
//            key_timestamps("创建时间")
//
//            fulfillment("开通学之道账号使用权限") {
//                request {
//                    key_timestamps("创建时间", "过期时间")
//                }
//
//                confirmation {
//                    key_timestamps("创建时间")
//
//                    this play grantAccess.confirmation
//                }
//            }
//        }
//    }
//
//    context("课程内容平台上下文") {
//        contract("课程内容平台服务协议") {
//            key_timestamps("创建时间")
//
//            fulfillment("查看教学资源", ONE_TO_N) {
//                request {
//                    key_timestamps("创建时间")
//                }
//
//                confirmation {
//                    key_timestamps("创建时间")
//
//                    this play viewCourses.confirmation
//                }
//            }
//        }
//    }
//
//    context("学习管理系统（LMS）上下文") {
//        contract("学习管理系统使用协议") {
//            key_timestamps("创建时间")
//
//            fulfillment("提供服务使用数据") {
//                request {
//                    key_timestamps("创建时间", "过期时间")
//                    key_data("视频播放量", "完播率")
//                }
//
//                confirmation {
//                    key_timestamps("创建时间")
//
//                    this play provideServiceData.confirmation
//                }
//            }
//        }
//    }

} export_doc "./docs/training/学校服务上下文.md"
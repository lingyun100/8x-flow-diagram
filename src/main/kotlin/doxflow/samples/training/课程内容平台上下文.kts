package doxflow.samples.training

import doxflow.diagram_8x_flow
import doxflow.models.diagram.Relationship.Companion.ONE_TO_N

diagram_8x_flow {
    context("课程内容平台上下文") {
        contract("课程内容平台服务协议") {
            key_timestamps("创建时间")

            fulfillment("存储教学资源", ONE_TO_N) {
                request {
                    key_timestamps("存储开始时间")
                }

                confirmation {
                    key_timestamps("存储完成时间")

                }
            }

            fulfillment("更新教学资源", ONE_TO_N) {
                request {
                    key_timestamps("更新开始时间")
                }

                confirmation {
                    key_timestamps("更新完成时间")

                }
            }

            fulfillment("删除教学资源", ONE_TO_N) {
                request {
                    key_timestamps("删除开始时间")
                }

                confirmation {
                    key_timestamps("删除完成时间")

                }
            }

            fulfillment("查看教学资源", ONE_TO_N) {
                request {
                    key_timestamps("查看开始时间")
                }

                confirmation {
                    key_timestamps("查看结束时间")

                }
            }
        }
    }
} export "./diagrams/training/课程内容平台上下文.png"
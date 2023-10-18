package doxflow.samples.training

import doxflow.diagram_8x_flow

diagram_8x_flow {
    context("学习管理系统（LMS）上下文") {
        contract("学校服务合同") {
            key_timestamps("创建时间")

            fulfillment("提供服务使用数据") {
                request {
                    key_timestamps("创建时间", "过期时间")
                    key_data("视频播放量", "完播率")
                }

                confirmation {
                    key_timestamps("创建时间")
                }
            }
        }
    }
} export "./diagrams/training/学习管理系统（LMS）上下文.png"
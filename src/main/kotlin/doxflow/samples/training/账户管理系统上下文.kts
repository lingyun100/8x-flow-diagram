package doxflow.samples.training

import doxflow.diagram_8x_flow

diagram_8x_flow {
    context("账户管理系统上下文") {
        contract("账户管理系统服务协议") {
            key_timestamps("创建时间")

            fulfillment("开通学之道账号使用权限") {
                request {
                    key_timestamps("申请时间", "过期时间")
                }

                confirmation {
                    key_timestamps("开通时间")
                }
            }
        }
    }
} export "./diagrams/training/账户管理系统上下文.png"
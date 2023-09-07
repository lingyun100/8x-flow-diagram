import doxflow.diagram_8x_flow

diagram_8x_flow {
    context("支付平台上下文") {
        contract("微信支付协议") {
            key_timestamps("创建时间")

            fulfillment("微信支付") {
                request {
                    key_timestamps("创建时间", "过期时间")
                }

                confirmation {
                    key_timestamps("创建时间")
                }
            }
        }
    }

} export "./diagrams/支付平台上下文.png"
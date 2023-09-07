import architecture.diagram_intra_process

diagram_intra_process {
    process("WebBFF或AppBFF").call("ResourceController", "Http请求/JSON")
    process("WebBFF或AppBFF").call("InvoicesVouchersController", "Http请求/JSON")
    process("WebBFF或AppBFF").call("PaymentController", "Http请求/JSON")
    process("WebBFF或AppBFF").call("ResourceInvitationController", "Http请求/JSON")

    layer("Controller层", "#HotPink") {
        component("ResourceController").call("ResourceService", "方法调用")
        component("InvoicesVouchersController").call("InvoicesVouchersService", "方法调用")
        component("PaymentController").call("PaymentService", "方法调用")
        component("ResourceInvitationController").call("ResourceInvitationService", "方法调用")
    }

    layer("Service层", "#orange") {
        component("ResourceService").call("ResourceClient", "方法调用")
        component("InvoicesVouchersService").call("InvoicesVouchersRepository", "方法调用")
        component("PaymentService").call("PaymentClient", "方法调用")
        component("PaymentService").call("CostCalculationClient", "方法调用")
        component("PaymentService").call("ThreadPoolManager", "方法调用")
        component("ResourceInvitationService").call("ResourceInvitationRepository", "方法调用")
    }

    layer("配置区", "#LightSeaGreen") {
        component("ThreadPoolManager")
    }

    layer("Repository层", "#LightSeaGreen") {
        val invoicesVouchersRepository = component("InvoicesVouchersRepository")
        val resourceInvitationRepository = component("ResourceInvitationRepository")

        invoicesVouchersRepository.call("Mysql", "读写")
        resourceInvitationRepository.call("Mysql", "读写")
    }

    layer("Manager通用处理层", "#LightSeaGreen") {
        val paymentClient = component("PaymentClient")
        val resourceClient = component("ResourceClient")
        val costCalculationClient = component("CostCalculationClient")

        paymentClient.call("微信支付", "Http请求/JSON")
        resourceClient.call("课程内容平台", "Http请求/octet-stream")
        costCalculationClient.call("费用计算领域服务", "Http请求/JSON")
    }

    process("Mysql")
    process("费用计算领域服务")
    process("课程内容平台")
    process("微信支付")


} export "./diagrams/resource_provider_process_diagram.png"
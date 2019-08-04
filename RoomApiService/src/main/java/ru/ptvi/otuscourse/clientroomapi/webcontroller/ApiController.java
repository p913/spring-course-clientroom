package ru.ptvi.otuscourse.clientroomapi.webcontroller;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.ptvi.otuscourse.clientroomapi.service.*;
import ru.ptvi.otuscourse.clientroomdto.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@Api(value="Integration api", description="Integration RestApi for third-party systems")
public class ApiController {
    private final ContragentApiService contragentApiService;

    private final ServiceApiService serviceApiService;

    private final DocumentApiService documentApiService;

    private final PayApiService payApiService;

    private final NotificationApiService notificationApiService;

    private final DemandApiService demandApiService;

    private final ChargesApiService chargesApiService;

    public ApiController(ContragentApiService contragentApiService, ServiceApiService serviceApiService,
                         DocumentApiService documentApiService, PayApiService payApiService,
                         NotificationApiService notificationApiService, DemandApiService demandApiService,
                         ChargesApiService chargesApiService) {
        this.contragentApiService = contragentApiService;
        this.serviceApiService = serviceApiService;
        this.documentApiService = documentApiService;
        this.payApiService = payApiService;
        this.notificationApiService = notificationApiService;
        this.demandApiService = demandApiService;
        this.chargesApiService = chargesApiService;
    }

    @GetMapping("/contragents")
    @ApiOperation(value = "Get all contragents")
    public List<ContragentWithDetailsDto> getContragents(@RequestParam(value = "account", required = false) @ApiParam("Email or phone") String account,
                                              @RequestParam(value = "details", required = false) @ApiParam("Include in response contracts and accounting objects") boolean details) {
        if (StringUtils.isEmpty(account))
            return contragentApiService.getContragents(details); // TODO details в тесты
        else
            return contragentApiService.getContragentByEmailOrPhone(account, details).stream().collect(Collectors.toList());
    }

    @GetMapping("/contragents/{cgid}")
    @ApiOperation(value = "Get contragent")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Founded contragent with contracts and accounting objects"),
            @ApiResponse(code = 404, message = "Contragent not found")
    })
    public ResponseEntity<ContragentDto> getContragent(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId) {
        var res = contragentApiService.getContragentById(contragentId);
        return res.isPresent()
                ? ResponseEntity.ok(res.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/contragents")
    @ApiOperation(value = "Create contragent. Returns new contragent id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Contragent created with id")
    })
    public ResponseEntity<String> createContragent(@RequestBody @ApiParam("Contragent") ContragentDto contragent) {
        contragentApiService.createContragent(contragent);
        return ResponseEntity
                .created(URI.create(String.format("/contragents/%s", contragent.id())))
                .body(contragent.id());
    }

    @PutMapping("/contragents/{cgid}")
    @ApiOperation(value = "Update contragent")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Contragent updated", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent not found")
    })
    public ResponseEntity updateContragent(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                           @RequestBody @ApiParam("Contragent") ContragentDto contragent) {
        contragent.id(contragentId);
        contragentApiService.updateContagent(contragent);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/contragents/{cgid}")
    @ApiOperation(value = "Patch contragent. Only not null or supplied fields will be modified. Useful for update password on client registration")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Contragent updated", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent not found")
    })
    public ResponseEntity patchContragent(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                           @RequestBody @ApiParam("Contragent") ContragentDto contragent) {
        contragent.id(contragentId);
        contragentApiService.patchContagent(contragent);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contragents/{cgid}")
    @ApiOperation(value = "Delete contragent")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Contract deleted", response = Object.class),
            @ApiResponse(code = 304, message = "Contract not deleted - no contract", response = Object.class),
            @ApiResponse(code = 409, message = "Conflict with references entities"),
    })
    public ResponseEntity deleteContract(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId) {
        return contragentApiService.deleteContragent(contragentId)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }



    @GetMapping("/contragents/{cgid}/contracts")
    @ApiOperation(value = "Get all contracts for contragent")
    public List<ContractDto> getContragentContracts(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId) {
        return contragentApiService.getContragentContracts(contragentId);
    }

    @GetMapping("/contragents/{cgid}/contracts/{ctid}")
    @ApiOperation(value = "Get contract with id for contragent")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Founded contract"),
            @ApiResponse(code = 404, message = "Contract not found")
    })
    public ResponseEntity<ContractDto> getContract(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                             @PathVariable("ctid") @ApiParam("Contract id") String contractId) {
        var res = contragentApiService.getContractById(contragentId, contractId);
        return res.isPresent()
                ? ResponseEntity.ok(res.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/contragents/{cgid}/contracts")
    @ApiOperation(value = "Create contract for contragent. Returns new contract id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Contract created with id"),
            @ApiResponse(code = 404, message = "Contragent not found"),
    })
    public ResponseEntity<String> createContract(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                 @RequestBody @ApiParam("Contract") ContractDto contract) {
        contragentApiService.createContract(contragentId, contract);
        return ResponseEntity
                .created(URI.create(String.format("/contragents/%s/contracts/%s", contragentId, contract.id())))
                .body(contract.id());
    }

    @PutMapping("/contragents/{cgid}/contracts/{ctid}")
    @ApiOperation(value = "Update contract")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Contract updated", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent or contract not found")
    })
    public ResponseEntity updateContract(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                         @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                         @RequestBody @ApiParam("Contract") ContractDto contract) {
        contract.id(contractId);
        contragentApiService.updateContract(contragentId, contract);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contragents/{cgid}/contracts/{ctid}")
    @ApiOperation(value = "Delete contract")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Contract deleted", response = Object.class),
            @ApiResponse(code = 304, message = "Contract not deleted - no contract", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent not found"),
            @ApiResponse(code = 409, message = "Conflict with references entities"),
    })
    public ResponseEntity deleteContract(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                         @PathVariable("ctid") @ApiParam("Contract id") String contractId) {
        return contragentApiService.deleteContract(contragentId, contractId)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }




    @GetMapping("/contragents/{cgid}/contracts/{ctid}/accobjects")
    @ApiOperation(value = "Get all accounting objects for contract")
    public List<AccountingObjectDto> getContractAccObjects(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                                           @PathVariable("ctid") @ApiParam("Contract id") String contractId) {
        return contragentApiService.getContractAccObjects(contragentId, contractId);
    }

    @GetMapping("/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}")
    @ApiOperation(value = "Get accounting object with id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Founded accounting object"),
            @ApiResponse(code = 404, message = "Accounting object not found")
    })
    public ResponseEntity<AccountingObjectDto> getAccObject(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                   @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                   @PathVariable("aoid") @ApiParam("Accounting object id") String accObjectId) {
        var res = contragentApiService.getAccObjectById(contragentId, contractId, accObjectId);
        return res.isPresent()
                ? ResponseEntity.ok(res.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/contragents/{cgid}/contracts/{ctid}/accobjects")
    @ApiOperation(value = "Create accounting object for contract. Returns new accounting object id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Accounting object created with id"),
            @ApiResponse(code = 404, message = "Contragent or contract not found"),
    })
    public ResponseEntity<String> createAccObject(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                                  @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                                 @RequestBody @ApiParam("Accounting object") AccountingObjectDto accountingObject) {
        contragentApiService.createAccObject(contragentId, contractId, accountingObject);
        return ResponseEntity
                .created(URI.create(String.format("/contragents/%s/contracts/%s/accobjects/%s", contragentId, contractId, accountingObject.id())))
                .body(accountingObject.id());
    }

    @PutMapping("/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}")
    @ApiOperation(value = "Update accounting object")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Accounting object updated", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent or contract or accounting object not found")
    })
    public ResponseEntity updateAccObject(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                          @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                          @PathVariable("aoid") @ApiParam("Accounting object id") String accObjectId,
                                          @RequestBody @ApiParam("Accounting object") AccountingObjectDto accountingObject) {
        accountingObject.id(accObjectId);
        contragentApiService.updateAccObject(contragentId, contractId, accountingObject);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}")
    @ApiOperation(value = "Delete accounting object")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Accounting object deleted", response = Object.class),
            @ApiResponse(code = 304, message = "Accounting object not deleted - no object", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent or contract not found"),
            @ApiResponse(code = 409, message = "Conflict with references entities")
    })
    public ResponseEntity deleteAccObject(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                         @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                          @PathVariable("aoid") @ApiParam("Accounting object id") String accObjectId) {
        return contragentApiService.deleteAccObject(contragentId, contractId, accObjectId)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @GetMapping("/services")
    @ApiOperation(value = "Get all services")
    public List<ServiceDto> getServices() {
        return serviceApiService.getAll();
    }

    @GetMapping("/services/{sid}")
    @ApiOperation(value = "Get service")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Founded service"),
            @ApiResponse(code = 404, message = "Service not found")
    })
    public ResponseEntity<ServiceDto> getService(@PathVariable("sid") @ApiParam("Service id") String serviceId) {
        var res = serviceApiService.getById(serviceId);
        return res.isPresent()
                ? ResponseEntity.ok(res.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/services")
    @ApiOperation(value = "Create service. Returns new service id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Service created with id")
    })
    public ResponseEntity<String> createService(@RequestBody @ApiParam("Service") ServiceDto service) {
        serviceApiService.create(service);
        return ResponseEntity
                .created(URI.create(String.format("/services/%s", service.id())))
                .body(service.id());
    }

    @PutMapping("/services/{sid}")
    @ApiOperation(value = "Update service")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Service updated", response = Object.class),
            @ApiResponse(code = 404, message = "Services not found")
    })
    public ResponseEntity updateService(@PathVariable("sid") @ApiParam("service id") String serviceId,
                                        @RequestBody @ApiParam("Service") ServiceDto service) {
        service.id(serviceId);
        serviceApiService.update(service);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/services/{sid}")
    @ApiOperation(value = "Delete service")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Service deleted", response = Object.class),
            @ApiResponse(code = 304, message = "Service not deleted - no service", response = Object.class),
            @ApiResponse(code = 409, message = "Conflict with references entities"),
    })
    public ResponseEntity deleteService(@PathVariable("sid") @ApiParam("service id") String serviceId) {
        return serviceApiService.delete(serviceId)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }




    @GetMapping("/contragents/{cgid}/contracts/{ctid}/documents")
    @ApiOperation(value = "Get all documents for contract")
    public List<DocuLinkDto> getContractDocuments(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                                  @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                                  @RequestParam("dateFrom") @ApiParam(required = true, value = "Start date (ISO-8601) for request documents", example = "2019-01-01") String dateFrom,
                                                  @RequestParam("dateTo") @ApiParam(required = true, value = "End date (ISO-8601) for request documents", example = "2019-01-31") String dateTo) {
        return documentApiService.getBetweenDates(contragentId, contractId, LocalDate.parse(dateFrom), LocalDate.parse(dateTo));
    }

    @GetMapping("/contragents/{cgid}/contracts/{ctid}/documents/{did}")
    @ApiOperation(value = "Get document with id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Founded document"),
            @ApiResponse(code = 404, message = "Document not found")
    })
    public ResponseEntity<DocuLinkDto> getDocument(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                            @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                            @PathVariable("did") @ApiParam("Document id") String documentId) {
        var res = documentApiService.getById(contragentId, contractId, documentId);
        return res.isPresent()
                ? ResponseEntity.ok(res.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/contragents/{cgid}/contracts/{ctid}/documents")
    @ApiOperation(value = "Create document for contract. Returns new document id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Document created with id"),
            @ApiResponse(code = 404, message = "Contragent or contract not found"),
    })
    public ResponseEntity<String> createDocument(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                                  @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                                  @RequestBody @ApiParam("Document") DocuLinkDto docuLink) {
        documentApiService.create(contragentId, contractId, docuLink);
        return ResponseEntity
                .created(URI.create(String.format("/contragents/%s/contracts/%s/documents/%s", contragentId, contractId, docuLink.id())))
                .body(docuLink.id());
    }

    @PutMapping("/contragents/{cgid}/contracts/{ctid}/documents/{did}")
    @ApiOperation(value = "Update document")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Document updated", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent or contract or document not found")
    })
    public ResponseEntity updateDocument(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                          @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                          @PathVariable("did") @ApiParam("Document id") String documentId,
                                          @RequestBody @ApiParam("Document") DocuLinkDto docuLink) {
        docuLink.id(documentId);
        documentApiService.update(contragentId, contractId, docuLink);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contragents/{cgid}/contracts/{ctid}/documents/{did}")
    @ApiOperation(value = "Delete document")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Document deleted", response = Object.class),
            @ApiResponse(code = 304, message = "Document not deleted - no document", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent or contract not found")
    })
    public ResponseEntity deleteDocument(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                          @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                          @PathVariable("did") @ApiParam("Document id") String documentId) {
        return documentApiService.delete(contragentId, contractId, documentId)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }



    @GetMapping("/contragents/{cgid}/contracts/{ctid}/pays")
    @ApiOperation(value = "Get all pays for contract")
    public List<PayDto> getContractPays(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                        @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                        @RequestParam("dateFrom") @ApiParam(required = true, value = "Start date (ISO-8601) for request pays", example = "2019-01-01") String dateFrom,
                                        @RequestParam("dateTo") @ApiParam(required = true, value = "End date (ISO-8601) for request pays", example = "2019-01-31") String dateTo) {
        return payApiService.getBetweenDates(contragentId, contractId, LocalDate.parse(dateFrom), LocalDate.parse(dateTo));
    }

    @GetMapping("/contragents/{cgid}/contracts/{ctid}/pays/{pid}")
    @ApiOperation(value = "Get pay with id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Founded pay"),
            @ApiResponse(code = 404, message = "Pay not found")
    })
    public ResponseEntity<PayDto> getPay(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                         @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                         @PathVariable("pid") @ApiParam("Pay id") String payId) {
        var res = payApiService.getById(contragentId, contractId, payId);
        return res.isPresent()
                ? ResponseEntity.ok(res.get())
                : ResponseEntity.notFound().build();

    }

    @PostMapping("/contragents/{cgid}/contracts/{ctid}/pays")
    @ApiOperation(value = "Create pay for contract. Returns new pay id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pay created with id"),
            @ApiResponse(code = 404, message = "Contragent or contract not found"),
    })
    public ResponseEntity<String> createPay(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                            @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                            @RequestBody @ApiParam("Pay") PayDto pay) {
        payApiService.create(contragentId, contractId, pay);
        return ResponseEntity
                .created(URI.create(String.format("/contragents/%s/contracts/%s/pays/%s", contragentId, contractId, pay.id())))
                .body(pay.id());
    }

    @PutMapping("/contragents/{cgid}/contracts/{ctid}/pays/{pid}")
    @ApiOperation(value = "Update pay")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pay updated", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent or contract or pay not found")
    })
    public ResponseEntity updatePay(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                    @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                    @PathVariable("pid") @ApiParam("Pay id") String payId,
                                    @RequestBody @ApiParam("Pay") PayDto pay) {
        pay.id(payId);
        payApiService.update(contragentId, contractId, pay);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contragents/{cgid}/contracts/{ctid}/pays/{pid}")
    @ApiOperation(value = "Delete pay")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Pay deleted", response = Object.class),
            @ApiResponse(code = 304, message = "Pay not deleted - no pay", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent or contract not found")
    })
    public ResponseEntity deletePay(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                    @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                    @PathVariable("pid") @ApiParam("Pay id") String payId) {
        return payApiService.delete(contragentId, contractId, payId)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }




    @GetMapping("/contragents/{cgid}/notifications")
    @ApiOperation(value = "Get all notifications for contragent. Get only unviewed notifications if dates not specified")
    public List<NotificationDto> getContragentNotifications(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                                            @RequestParam(name = "dateFrom", required = false) @ApiParam(required = false, value = "Start date (ISO-8601) for request notifications", example = "2019-01-01") String dateFrom,
                                                            @RequestParam(name = "dateTo", required = false) @ApiParam(required = false, value = "End date (ISO-8601) for request notifications", example = "2019-01-31") String dateTo) {

        if (StringUtils.isEmpty(dateFrom) || StringUtils.isEmpty(dateTo))
            return notificationApiService.getUnviewed(contragentId);
        else
            return notificationApiService.getBetweenDates(contragentId, LocalDate.parse(dateFrom), LocalDate.parse(dateTo));
    }

    @GetMapping("/contragents/{cgid}/notifications/{nid}")
    @ApiOperation(value = "Get notification with id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Founded notification"),
            @ApiResponse(code = 404, message = "Notification not found")
    })
    public ResponseEntity<NotificationDto> getNotification(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                           @PathVariable("nid") @ApiParam("Notification id") String notificationId) {
        var res = notificationApiService.getById(contragentId, notificationId);
        return res.isPresent()
                ? ResponseEntity.ok(res.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/contragents/{cgid}/notifications")
    @ApiOperation(value = "Create notification for contragent. Returns new notification id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Notification created with id"),
            @ApiResponse(code = 404, message = "Contragent not found"),
    })
    public ResponseEntity<String> createNotification(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                                     @RequestBody @ApiParam("Notification") NotificationDto notification) {
        notificationApiService.create(contragentId, notification);
        return ResponseEntity
                .created(URI.create(String.format("/contragents/%s/notifications/%s", contragentId, notification.id())))
                .body(notification.id());
    }

    @PutMapping("/contragents/{cgid}/notifications/{nid}")
    @ApiOperation(value = "Update notification")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Notification updated", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent or notification not found")
    })
    public ResponseEntity updateNotification(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                             @PathVariable("nid") @ApiParam("Notification id") String notificationId,
                                             @RequestBody @ApiParam("Notification") NotificationDto notification) {
        notification.id(notificationId);
        notificationApiService.update(contragentId, notification);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contragents/{cgid}/notifications/{nid}")
    @ApiOperation(value = "Delete notification")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Notification deleted", response = Object.class),
            @ApiResponse(code = 304, message = "Notification not deleted - no notification", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent not found")
    })
    public ResponseEntity deleteNotification(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                             @PathVariable("nid") @ApiParam("Notification id") String notificationId) {
        return notificationApiService.delete(contragentId, notificationId)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }



    @GetMapping("/contragents/{cgid}/demands")
    @ApiOperation(value = "Get all demands for contragent")
    public List<DemandDto> getContragentDemands(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                                @RequestParam("dateFrom") @ApiParam(required = true, value = "Start date (ISO-8601) for request demands", example = "2019-01-01") String dateFrom,
                                                @RequestParam("dateTo") @ApiParam(required = true, value = "End date (ISO-8601) for request demands", example = "2019-01-31") String dateTo) {
        return demandApiService.getBetweenDates(contragentId, LocalDate.parse(dateFrom), LocalDate.parse(dateTo));
    }

    @GetMapping("/contragents/{cgid}/demands/{dmid}")
    @ApiOperation(value = "Get demand with id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Founded demand"),
            @ApiResponse(code = 404, message = "Demand not found")
    })
    public ResponseEntity<DemandDto> getDemand(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                               @PathVariable("dmid") @ApiParam("Demand id") String demandId) {
        var res = demandApiService.getById(contragentId, demandId);
        return res.isPresent()
                ? ResponseEntity.ok(res.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/contragents/{cgid}/demands")
    @ApiOperation(value = "Create demand for contragent. Returns new demand id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Demand created with id"),
            @ApiResponse(code = 404, message = "Contragent not found"),
    })
    public ResponseEntity<String> createDemand(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                               @RequestBody @ApiParam("Demand") DemandDto demand) {
        demandApiService.create(contragentId, demand);
        return ResponseEntity
                .created(URI.create(String.format("/contragents/%s/demands/%s", contragentId, demand.id())))
                .body(demand.id());
    }

    @PutMapping("/contragents/{cgid}/demands/{dmid}")
    @ApiOperation(value = "Update demand")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Demand updated", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent or demand not found")
    })
    public ResponseEntity updateDemand(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                       @PathVariable("dmid") @ApiParam("Demand id") String demandId,
                                       @RequestBody @ApiParam("Demand") DemandDto demand) {
        demand.id(demandId);
        demandApiService.update(contragentId, demand);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contragents/{cgid}/demands/{dmid}")
    @ApiOperation(value = "Delete demand")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Demand deleted", response = Object.class),
            @ApiResponse(code = 304, message = "Demand not deleted - no demand", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent not found")
    })
    public ResponseEntity deleteDemand(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                       @PathVariable("dmid") @ApiParam("Demand id") String demandId) {
        return demandApiService.delete(contragentId, demandId)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }



    @GetMapping("/contragents/{cgid}/contracts/{ctid}/charges")
    @ApiOperation(value = "Get all one-time charges for contract")
    public List<ChargeOnceDto> getContractChargeOnces(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                                      @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                                      @RequestParam("dateFrom") @ApiParam(required = true, value = "Start date (ISO-8601) for request charges", example = "2019-01-01") String dateFrom,
                                                      @RequestParam("dateTo") @ApiParam(required = true, value = "End date (ISO-8601) for request charges", example = "2019-01-31") String dateTo) {
        return chargesApiService.getChargeOnceBetweenDates(contragentId, contractId, LocalDate.parse(dateFrom), LocalDate.parse(dateTo));
    }

    @GetMapping("/contragents/{cgid}/contracts/{ctid}/charges/{chid}")
    @ApiOperation(value = "Get one-time charge with id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Founded one-time charge"),
            @ApiResponse(code = 404, message = "One-time charge not found")
    })
    public ResponseEntity<ChargeOnceDto> getChargeOnce(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                       @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                       @PathVariable("chid") @ApiParam("One-time charge id") String chargeId) {
        var res =  chargesApiService.getChargeOnceById(contragentId, contractId, chargeId);
        return res.isPresent()
                ? ResponseEntity.ok(res.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/contragents/{cgid}/contracts/{ctid}/charges")
    @ApiOperation(value = "Create one-time charge for contract. Returns new charge id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "One-time charge created with id"),
            @ApiResponse(code = 404, message = "Contragent or contract not found"),
    })
    public ResponseEntity<String> createChargeOnce(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                                   @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                                   @RequestBody @ApiParam("One-time charge") ChargeOnceDto charge) {
        chargesApiService.createChargeOnce(contragentId, contractId, charge);
        return ResponseEntity
                .created(URI.create(String.format("/contragents/%s/contracts/%s/charges/%s", contragentId, contractId, charge.id())))
                .body(charge.id());
    }

    @PutMapping("/contragents/{cgid}/contracts/{ctid}/charges/{chid}")
    @ApiOperation(value = "Update one-time charge")
    @ApiResponses({
            @ApiResponse(code = 200, message = "One-time charge updated", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent or contract or charge not found")
    })
    public ResponseEntity updateChargeOnce(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                           @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                           @PathVariable("chid") @ApiParam("One-time charge id") String chargeId,
                                           @RequestBody @ApiParam("One-time charge") ChargeOnceDto charge) {
        charge.id(chargeId);
        chargesApiService.updateChargeOnce(contragentId, contractId, charge);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contragents/{cgid}/contracts/{ctid}/charges/{chid}")
    @ApiOperation(value = "Delete one-time charge")
    @ApiResponses({
            @ApiResponse(code = 204, message = "One-time charge deleted", response = Object.class),
            @ApiResponse(code = 304, message = "One-time charge not deleted - no charge", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent or contract not found")
    })
    public ResponseEntity deleteChargeOnce(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                           @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                           @PathVariable("chid") @ApiParam("One-time charge id") String chargeId) {
        return chargesApiService.deleteChargeOnce(contragentId, contractId, chargeId)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @GetMapping("/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges")
    @ApiOperation(value = "Get all service charges for contract")
    public List<ChargeServiceDto> getContractChargeServices(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                                            @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                                            @PathVariable("aoid") @ApiParam("Accounting object id") String accObjectId,
                                                            @RequestParam("dateFrom") @ApiParam(required = true, value = "Start date (ISO-8601) for request charges", example = "2019-01-01") String dateFrom,
                                                            @RequestParam("dateTo") @ApiParam(required = true, value = "End date (ISO-8601) for request charges", example = "2019-01-31") String dateTo) {
        return chargesApiService.getChargeServiceBetweenDates(contragentId, contractId, accObjectId, LocalDate.parse(dateFrom), LocalDate.parse(dateTo));
    }

    @GetMapping("/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}")
    @ApiOperation(value = "Get service charge with id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Founded service charge"),
            @ApiResponse(code = 404, message = "Service charge not found")
    })
    public ResponseEntity<ChargeServiceDto> getChargeService(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                             @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                             @PathVariable("aoid") @ApiParam("Accounting object id") String accObjectId,
                                             @PathVariable("chid") @ApiParam("Service charge id") String chargeId) {
        var res = chargesApiService.getChargeServiceById(contragentId, contractId, accObjectId, chargeId);
        return res.isPresent()
                ? ResponseEntity.ok(res.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges")
    @ApiOperation(value = "Create service charge for contract. Returns new charge id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Service charge created with id"),
            @ApiResponse(code = 404, message = "Contragent or contract or accounting object not found"),
    })
    public ResponseEntity<String> createChargeService(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                                      @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                                      @PathVariable("aoid") @ApiParam("Accounting object id") String accObjectId,
                                                      @RequestBody @ApiParam("Service charge") ChargeServiceDto charge) {
        chargesApiService.createChargeService(contragentId, contractId, accObjectId, charge);
        return ResponseEntity
                .created(URI.create(String.format("/contragents/%s/contracts/%s/accobjects/%s/charges/%s",
                            contragentId, contractId, accObjectId, charge.id())))
                .body(charge.id());
    }

    @PutMapping("/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}")
    @ApiOperation(value = "Update service charge")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Service charge updated", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent or contract or accounting object or charge not found")
    })
    public ResponseEntity updateChargeService(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                              @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                              @PathVariable("aoid") @ApiParam("Accounting object id") String accObjectId,
                                              @PathVariable("chid") @ApiParam("Service charge id") String chargeId,
                                              @RequestBody @ApiParam("Service charge") ChargeServiceDto charge) {
        charge.id(chargeId);
        chargesApiService.updateChargeService(contragentId, contractId, accObjectId, charge);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contragents/{cgid}/contracts/{ctid}/accobjects/{aoid}/charges/{chid}")
    @ApiOperation(value = "Delete service charge")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Service charge deleted", response = Object.class),
            @ApiResponse(code = 304, message = "Service charge not deleted - no charge", response = Object.class),
            @ApiResponse(code = 404, message = "Contragent or contract or accounting object not found")
    })
    public ResponseEntity deleteChargeService(@PathVariable("cgid") @ApiParam("Contragent id") String contragentId,
                                              @PathVariable("ctid") @ApiParam("Contract id") String contractId,
                                              @PathVariable("aoid") @ApiParam("Accounting object id") String accObjectId,
                                              @PathVariable("chid") @ApiParam("Service charge id") String chargeId) {
        return chargesApiService.deleteChargeService(contragentId, contractId, accObjectId, chargeId)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

}

var model = {
  contragent: {
    email: "-",
    address: "-"
  },
  charges: {},
  pays: {},
  docs: {},
  notifications: [],
  newNotificationCount: 0,
  pages: [],
  page: {},
  demands: {},
  company: '-',
  contacts: '-'
};

function addWithUniqueId(to, from) {
    var added = 0;
    from.forEach(f => {
        var founded = false;
        to.forEach(t => { if (f.id == t.id) founded = true} );
        if (!founded) {
          to.push(f);
          added++;
        }
    })
    return added;
}

function PageContragent() {
    this.title = "Договора"
    this.component = {
      methods: {
        loadContragentInfo() {
          fetch('/room/contragent')
            .then(response => {
                      if (!response.ok) throw response;
                      return response.json()
                    })
            .then(data => { model.contragent = data; });
        }
      },
      template: '<contragent-info id="contragent-info" v-bind:contragent="$parent.contragent"></contragent-info>'
    };
    this.init = this.component.methods.loadContragentInfo;
    this.activate = function() {};
    this.deactivate = function() {};
};

function PageCharges() {
    this.title = "Начисления";
    this.component = {
      methods: {
        loadCharges(dateFrom, dateTo) {
          fetch('/room/charges?dateFrom=' + dateFrom + '&dateTo=' + dateTo)
              .then(response => {
                        if (!response.ok) throw response;
                        return response.json()
                      })
              .then(data => { model.charges = data; });
        }
      },
      template: `<div class="page-charges">
        <date-diap-selector class="page-charges__dates" v-on:selected="loadCharges"></date-diap-selector>
        <charge-once-list class="page-charges__service-data" v-bind:charges="$parent.charges.chargeOnce"></charge-once-list>
        <charge-service-list class="page-charges__once-data" v-bind:charges="$parent.charges.chargeService"></charge-service-list>
      </div>`
    };
    this.init = function() {
          fetch('/room/charges')
              .then(response => {
                        if (!response.ok) throw response;
                        return response.json()
                      })
              .then(data => { model.charges = data; });
    }
    this.activate = function() {};
    this.deactivate = function() {};
};

function PagePays() {
    this.title = "Оплаты";
    this.component = {
      methods: {
        loadPays(dateFrom, dateTo) {
          fetch('/room/pays?dateFrom=' + dateFrom + '&dateTo=' + dateTo)
              .then(response => {
                            if (!response.ok) throw response;
                            return response.json()
                          })
              .then(data => { model.pays = data; });
        }
      },
      mounted: function() {
        $('#page-pays__button-pay').click(() => window.open('http://127.0.0.1:8080/newpay'))
      },
      template: `<div class="page-pays">
        <date-diap-selector class="page-pays__dates" v-on:selected="loadPays"></date-diap-selector>
        <pay-list class="page-pays__pays-data" v-bind:pays="$parent.pays"></pay-list>
        <button id="page-pays__button-pay" class="page-pays__button-pay ui-button ui-corner-all">Оплатить</button>
      </div>`
    };
    this.init = function() {
        fetch('/room/pays')
            .then(response => {
                          if (!response.ok) throw response;
                          return response.json()
                        })
            .then(data => { model.pays = data; });
    };
    this.activate = function() {};
    this.deactivate = function() {};
};

function PageDocs() {
    this.title = "Документы";
    this.component = {
      methods: {
        loadDocs(dateFrom, dateTo) {
          fetch('/room/documents?dateFrom=' + dateFrom + '&dateTo=' + dateTo)
              .then(response => {
                            if (!response.ok) throw response;
                            return response.json()
                          })
              .then(data => { model.docs = data; });
        }
      },
      template: `<div class="page-docs">
        <date-diap-selector class="page-docs__dates" v-on:selected="loadDocs"></date-diap-selector>
        <document-list class="page-docs__docs-data" v-bind:docs="$parent.docs"></document-list>
      </div>`
    };
    this.init = function() {
          fetch('/room/documents')
              .then(response => {
                            if (!response.ok) throw response;
                            return response.json()
                          })
              .then(data => { model.docs = data; });
    };
    this.activate = function() {};
    this.deactivate = function() {};
};

function PageNotifications() {
    this.title = "Уведомления";
    this.isNotification = true;
    this.idNotifications = [];
    this.component = {
      methods: {
        loadNotifications(dateFrom, dateTo) {
          fetch('/room/notifications?dateFrom=' + dateFrom + '&dateTo=' + dateTo)
              .then(response => {
                            if (!response.ok) throw response;
                            return response.json()
                          })
              .then(data => { model.notifications = data; });
        },
      },
      template: `<div class="page-nots">
        <date-diap-selector class="page-nots__dates" v-on:selected="loadNotifications"></date-diap-selector>
        <notification-list class="page-nots__nots-data" v-bind:notifications="$parent.notifications"></notification-list>
      </div>`
    };
    this.init = function() {
        fetch('/room/notifications')
              .then(response => {
                            if (!response.ok) throw response;
                            return response.json()
                          })
              .then(data => { model.newNotificationCount = data.length; model.notifications = data; });
        setTimeout(this.update, 30000, this);
    };
    this.update = function(that) {
        fetch('/room/notifications')
          .then(response => {
                            if (!response.ok) throw response;
                            return response.json()
                          })
          .then(data => model.newNotificationCount += addWithUniqueId(model.notifications, data));
        setTimeout(that.update, 30000, that)
    };
    this.viewed = function() {
        var notifications = [];
        model.notifications.forEach(function(n) {
              if(!n.viewed) notifications.push(n.id)
            }
        );
        if (notifications.length)
            fetch('/room/notifications', {
                method: 'put',
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(notifications)}
        );
    }
    this.activate = function() {
        this.viewed();
        model.newNotificationCount = 0;
    };
    this.deactivate = function() {
        this.viewed();
        model.newNotificationCount = 0;
        model.notifications.forEach(function(n) { n.viewed = true });
    }
};


function PageDemands() {
    this.title = "Заявки";
    this.component = {
      data: function() {
        return {
          error: ''
        }
      },
      methods: {
        loadDemands(dateFrom, dateTo) {
          fetch('/room/demands?dateFrom=' + dateFrom + '&dateTo=' + dateTo)
              .then(response => {
                            if (!response.ok) throw response;
                            return response.json()
                          })
              .then(data => { model.demands = data; });
        },
        pushDemand(demand) {
          this.error = '';
          fetch('/room/demand', {
              method: 'post',
              headers: {
                  'Accept': 'application/json, text/plain, */*',
                  'Content-Type': 'application/json'
                },
                body: JSON.stringify(demand)
            }).then(response => { if (response.status == 201) {
                                      $('#page-demands-push-form').hide();
                                      this.loadDemands('', '');
                                   } else
                                      this.error = 'Заявка не может быть добавлена в таком виде, попробуйте изменить ее';
                                 } );
        },
      },
      computed: {
        accObjects() {
            var res = [{}]
            model.contragent.contracts.forEach(c => c.accObjects.forEach(ct => res.push(ct)));
            return res;
        },
        contracts() {
            return [{}].concat(model.contragent.contracts) ;
        }
      },
      mounted: function() {
        $('#page-demands-push-form').hide();
        $('#page-demands-show-from-button').click(() => $('#page-demands-push-form').show());
      },
      template: `<div class="page-demands">
        <date-diap-selector class="page-demands__dates" v-on:selected="loadDemands"></date-diap-selector>
        <demand-list class="page-demands__demonds-data" v-bind:demands="$parent.demands"></demand-list>
        <button class="page-demands__send-button ui-button ui-corner-all" id="page-demands-show-from-button">Подать заявку</button>
        <section class="page-demands__error ui-state-error ui-corner-all" v-if="error" id="#page-demands-push-error">
          <p>{{ error }}</p>
        </section>
        <demand-push-form class="page-demands__send-form" id="page-demands-push-form" v-bind:contracts="contracts"
            v-bind:accObjects="accObjects" v-bind:validationErrors="{}" v-on:save="pushDemand">
        </demand-push-form>
      </div>`
    };
    this.init = function() {
          fetch('/room/demands')
              .then(response => {
                            if (!response.ok) throw response;
                            return response.json()
                          })
              .then(data => { model.demands = data; });
    };
    this.activate = function() {};
    this.deactivate = function() {};
};

model.pages = [
  new PageContragent(),
  new PageCharges(),
  new PagePays(),
  new PageDocs(),
  new PageNotifications(),
  new PageDemands()
];
model.page = model.pages[0];

$(
  function() {
        vm = new Vue({
            el: '#room',
            data: model,
            watch: {
              page: function(newPage, oldPage) {
                oldPage.deactivate();
                newPage.activate();
              }
            }
        });

        init();
  }
);

function init() {
    fetch('/reg/company')
              .then(response => {
                        if (!response.ok) throw response;
                        return response.json()
                      })
              .then(data => {
                    model.company = data.name;
                    model.contacts = data.contacts;
                });

    model.pages.forEach(function(i) {i.init();})
}

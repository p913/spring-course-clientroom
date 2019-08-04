const datepickerOptions = {
    dateFormat: $.datepicker.ISO_8601
}

Vue.filter('formatLocalDateTime', function(value) {
  if (value) {
    if (!value)
      return '';
    var dt = new Date(value);
    var fmt2 = (n) => { return ("" + (100 + n)).substring(1)};
    return `${dt.getFullYear()}-${fmt2(dt.getMonth()+ 1)}-${fmt2(dt.getDate())} ${dt.getHours()}:${fmt2(dt.getMinutes())}`;
  }
});

Vue.component('contragent-info', {
  props: ['contragent'],
  template: `
    <section class="c-contragent-info">
        <p>E-mail: <span class="c-contragent-info__accent">{{ contragent.email }}</span></p>
        <section v-if="contragent.peopleId">
          <p>ФИО: <span class="c-contragent-info__accent">{{ contragent.peopleFirstName }} {{ contragent.peopleMiddleName }} {{ contragent.peopleLastName }}</span></p>
          <p>Паспорт: <span class="c-contragent-info__accent">{{ contragent.peoplePassport }}</span></p>
          <p>Дата рождения: <span class="c-contragent-info__accent">{{ contragent.peopleBirthday }}</span></p>
        </section>
        <section v-if="contragent.firmId">
          <p>Наименование: <span class="c-contragent-info__accent">{{ contragent.firmName }}</span></p>
          <p v-if="contragent.firmInn && contragent.firmKpp">ИНН/КПП: <span class="c-contragent-info__accent">{{ contragent.firmInn }} / {{ contragent.firmKpp }}</span></p>
          <p v-if="contragent.firmAccount && contragent.firmBank">Расчетный счет: <span class="c-contragent-info__accent">{{ contragent.firmAccount }} в {{ contragent.firmBank }}</span></p>
        </section>
        <p>Адрес для корреспонденции: <span class="c-contragent-info__accent">{{ contragent.address }}</span></p>
        <contragent-contract v-for="contract in contragent.contracts" v-bind:key="contract.number" v-bind:contract="contract">
        </contragent-contract>
      </section>`
});

Vue.component('contragent-contract', {
  props: ['contract'],
  template: `
    <section class="c-contragent-contract">
      <header>
        Договор: <span class="c-contragent-contract__accent">{{ contract.number }}</span>
        от <span class="c-contragent-contract__accent">{{ contract.dateFrom }}</span>
        <span v-if="contract.dateTo"> до <span class="c-contragent-contract__accent">{{ contract.dateTo }}</span></span>
        Баланс: <span class="c-contragent-contract__balance" v-bind:class="{ 'c-contragent-contract__balance--negative': contract.balance < 0 }">{{ contract.balance }}</span>
      </header>
      <contragent-contract-acc-object v-for="obj in contract.accObjects" v-bind:key="obj.name" v-bind:accObject="obj">
      </contragent-contract-acc-object>
    </section>`
});

Vue.component('contragent-contract-acc-object', {
  props: ['accObject'],
  template: `
  <section class="c-contragent-contract-acc-object">
    <p class="c-contragent-contract-acc-object__name">Объект учета: <span class="c-contragent-contract-acc-object__accent">{{ accObject.name }}</span></p>
    <p class="c-contragent-contract-acc-object__description" v-if="accObject.description"><span class="c-contragent-contract-acc-object__accent">{{ accObject.description }}</span><p>
    <p class="c-contragent-contract-acc-object__dates">Дата начала использования: <span class="c-contragent-contract-acc-object__accent">{{ accObject.dateFrom }}</span>
       <span v-if="accObject.dateTo">, дата окончания <span class="c-contragent-contract-acc-object__accent">{{ contract.dateTo }}</span></span></p>
    <p class="c-contragent-contract-acc-object__service">Услуга: <span class="c-contragent-contract-acc-object__accent">{{ accObject.service.name }}</span>,
        абонплата: <span class="c-contragent-contract-acc-object__accent"> {{ accObject.service.cost }}</span> </p>
    </section>
  `
});

Vue.component('date-diap-selector', {
  data: function() {
    return {
       dateFrom: '',
       dateTo: '',
       idFrom: this._uid + '-date-from',
       idTo: this._uid + '-date-to'
    }
  },
  mounted: function() {
    $('#' + this.idFrom).datepicker(datepickerOptions);
    $('#' + this.idTo).datepicker(datepickerOptions);
  },
  methods: {
     emitDatesEvent: function() {
       this.$emit('selected', this.dateFrom, this.dateTo)
     }
  },
  template: `
    <form class="c-date-diap-selector" v-on:submit.prevent="emitDatesEvent">
      С <input class="c-date-diap-selector__input-date-from ui-corner-all" type="text" v-model="dateFrom"  v-bind:id="idFrom" required="true">
      По <input class="c-date-diap-selector__input-date-to ui-corner-all" type="text" v-model="dateTo"v-bind:id="idTo" required="true">
      <button class="c-date-diap-selector__button-apply ui-button ui-corner-all">Показать</button>
    </form>`
});


Vue.component('charge-once-list', {
  props: ['charges'],
  template: `
  <table class="c-charge-once-list table-simple-data">
    <tr> <th>Договор</th> <th>С</th> <th>По</th> <th>Работы, услуги</th> <th>Ед.изм</th> <th>Тариф</th> <th>Кол-во</th> <th>Стоимость</th> </tr>
    <tr v-for="c in charges" v-bind:key="c.id">
       <td>{{ c.contract.number }}</td>
       <td>{{ c.dateFrom }}</td>
       <td>{{ c.dateTo }}</td>
       <td>{{ c.description }}</td>
       <td>{{ c.metric }}</td>
       <td>{{ c.quantity }}</td>
       <td>{{ c.cost }}</td>
       <td>{{ c.summ }}</td>
    </tr>
  </table>
  `
});

Vue.component('charge-service-list', {
  props: ['charges'],
  template: `
  <table class="c-charge-service-list table-simple-data">
    <tr> <th>Объект учета</th> <th>С</th> <th>По</th> <th>Услуга</th> <th>Ед.изм</th> <th>Тариф</th> <th>Кол-во</th> <th>Стоимость</th> </tr>
    <tr v-for="c in charges" v-bind:key="c.id">
       <td>{{ c.accObject.name }}</td>
       <td>{{ c.dateFrom }}</td>
       <td>{{ c.dateTo }}</td>
       <td>{{ c.service.name }}</td>
       <td>{{ c.metric }}</td>
       <td>{{ c.quantity }}</td>
       <td>{{ c.cost }}</td>
       <td>{{ c.summ }}</td>
    </tr>
  </table>
  `
});


Vue.component('pay-list', {
  props: ['pays'],
  template: `
  <table class="c-pay-list table-simple-data">
    <tr> <th>Договор</th> <th>Дата</th> <th>Сумма</th> <th>Источник</th> <th>№ транзакци</th> <th>№ документа</th></tr>
    <tr v-for="p in pays" v-bind:key="p.id">
       <td>{{ p.contract.number }}</td>
       <td>{{ p.payDateTime | formatLocalDateTime }}</td>
       <td>{{ p.summ }}</td>
       <td>{{ p.source }}</td>
       <td>{{ p.transactionNumber }}</td>
       <td>{{ p.documentNumber }}</td>
    </tr>
  </table>
  `
});

Vue.component('document-list', {
  props: ['docs'],
  template: `
  <table class="c-document-list table-simple-data">
    <tr> <th>Договор</th> <th>Дата/время</th> <th>Название</th> <th> </th> </tr>
    <tr v-for="d in docs" v-bind:key="d.id">
       <td>{{ d.contract.number }}</td>
       <td>{{ d.date }}</td>
       <td>{{ d.title }}</td>
       <td><a target="_blank" v-bind:href="d.url">Скачать</a></td>
    </tr>
  </table>
  `
});

Vue.component('notification-list', {
  props: ['notifications'],
  template: `
  <section class="c-notification-list">
    <article class="c-notification-list__notification ui-corner-all" v-bind:class="{'c-notification-list__notification--new': !n.viewed}"
            v-for="n in notifications" v-bind:key="n.id">
      <p class="c-notification-list__text"> {{ n.message }}</p>
      <footer class="c-notification-list__footer-date">{{ n.originDateTime | formatLocalDateTime }}</footer>
    </article>
  </section>`
});

Vue.component('demand-list', {
  props: ['demands'],
  data: function() {
    return {
        subjects: {
          'PAUSE': 'Приостановить',
          'RESUME': 'Возобновить',
          'STOP': 'Отключить',
          'SERVICE': 'Сменить услугу',
          'OTHER': 'Другое'
        }
    }
  },
  template: `
  <table class="c-demand-list table-simple-data">
    <tr> <th>Заявка выполнена</th> <th>Тип заявки</th> <th>Содержание заявки</th> <th>Дата/время заявки</th>
         <th>Договор</th> <th>Объект учета</th> <th>Дата/время решения</th> <th>Пояснение к решению</th> </tr>
    <tr v-for="d in demands" v-bind:key="d.id">
       <td>{{ d.decisionSuccess ? 'Да' : 'Нет'}}</td>
       <td>{{ subjects[d.demandSubject] }}</td>
       <td>{{ d.demandNote }}</td>
       <td>{{ d.demandDateTime | formatLocalDateTime }}</td>
       <td>{{ d.contract ? d.contract.number : '' }}</td>
       <td>{{ d.accObject ? d.accObject.name : '' }}</td>
       <td>{{ d.decisionDateTime | formatLocalDateTime }}</td>
       <td>{{ d.decisionNote }}</td>
    </tr>
  </table>
  `
});

Vue.component('demand-push-form', {
  props: ['contracts', 'accObjects', 'validationErrors'],
  data: function() {
    return {
        demand: {
          contract: {},
          accObject: {},
        },
        subjects: [
          { key: 'PAUSE', title: 'Приостановить пользование и абонплату' },
          { key: 'RESUME', title: 'Возобновить пользование и абонплату' },
          { key: 'STOP', title: 'Отключить полностью / отказ от пользования)' },
          { key: 'SERVICE', title: 'Сменить услугу (ниже полностью укажите желаемую услугу)' },
          { key: 'OTHER', title: 'Другое (ниже опишите как можно подробнее)' }
        ]
    }
  },
  template: `
  <form class="c-demand-push-form" v-on:submit.prevent="$emit('save', demand)">
    <div class="demand-push-form__contract">
      <label class="demand-push-form__label-contract" for="demand-push-form-contract">Договор: </label>
      <select class="demand-push-form__input-contract ui-corner-all" id="demand-push-form-contract" v-model="demand.contract.id">
        <option v-for="contract in contracts" v-bind:value="contract.id" v-bind:key="contract.id">{{ contract.number }}</option>
      </select>
    </div>
    <div class="demand-push-form__acc-object">
      <label class="demand-push-form__label-acc-object"  for="demand-push-form-acc-object">Объект учета: </label>
      <select class="demand-push-form__input-acc-object ui-corner-all" id="demand-push-form-acc-object" v-model="demand.accObject.id">
        <option v-for="accObject in accObjects" v-bind:value="accObject.id" v-bind:key="accObject.id">{{ accObject.name }}</option>
      </select>
    </div>
    <div class="demand-push-form__subject">
      <label class="demand-push-form__label-subject" for="demand-push-form-subject">Тип заявки: </label>
      <select class="demand-push-form__input-subject ui-corner-all" id="demand-push-form-subject" v-model="demand.demandSubject" required>
        <option v-for="subj in subjects" v-bind:value="subj.key" v-bind:key="subj.key">{{ subj.title }}</option>
      </select>
    </div>
    <div class="demand-push-form__note">
      <label class="demand-push-form__label-note" for="demand-push-form-demand-note">Содержание заявки: </label>
      <textarea class="demand-push-form__input-note ui-corner-all" id="demand-push-form-demand-note" v-model="demand.demandNote" rows="5" required></textarea>
    </div>
    <div class="demand-push-form__button-send">
      <button class="ui-button ui-corner-all">Отправить</button>
    </div>
  </form>
`
});

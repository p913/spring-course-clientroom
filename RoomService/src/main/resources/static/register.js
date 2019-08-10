$(function() {
    vm = new Vue({
        el: '#register',
        methods: {
          register: function() {
              fetch('/reg/register', {
                method: 'post',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(this.reg)
              })
              .then(response => {
                            if (!response.ok) throw response;
                            return response.json()
              })
              .then(data => {
                        if (data.success)
                            window.location = "/";
                        else
                            this.error = data.error;
              });
          }
        },
        data: {
            reg: {
                username: '',
                password: '',
                contract: '',
                date: ''
            },
            error: ''
        },
    });
});


document.addEventListener('DOMContentLoaded', function () {
    const formsWithConfirm = document.querySelectorAll("form[data-form-confirm]");

    formsWithConfirm.forEach(form => {
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            (async () => {
                const result = await b_confirm()
                if (result) {
                    form.submit();
                }
            })();
        });
    });
});

async function b_confirm() {
    const modalElem = document.createElement('div');
    modalElem.id = "modal-confirm";
    modalElem.className = "modal fade modal-zoom";
    modalElem.innerHTML = `
                            <div class="modal-dialog modal-dialog-centered">
                              <div class="modal-content p-2 pt-3 rounded-4">
                                <div class="modal-body text-center">
                                  <h5>Вы уверены, что хотите выполнить это действие?</h5>
                                  <p class="text-secondary mb-1">Данное действие невозможно будет отменить</p>
                                </div>
                                <div class="modal-footer border-top-0">
                                  <button id="modal-btn-cancel" type="button" class="btn btn-outline-dark mx-2 py-2 rounded-3 flex-grow-1">
                                    Отменить
                                  </button>
                                  <button id="modal-btn-accept" type="button" class="btn btn-dark mx-2 py-2 rounded-3 flex-grow-1">
                                    Подтвердить
                                  </button>
                                </div>
                              </div>
                            </div>
                          `;
    const modalConfirm = new bootstrap.Modal(modalElem, {
        backdrop: 'static'
    });
    modalConfirm.show();

    return new Promise((resolve, reject) => {
        document.body.addEventListener('click', response);

        function response(e) {
            let bool = false;
            if (e.target.id === 'modal-btn-cancel') bool = false;
            else if (e.target.id === 'modal-btn-accept') bool = true;
            else return;

            modalConfirm.hide();
            document.body.removeEventListener('click', response);
            document.body.querySelector('.modal-backdrop').remove();
            modalElem.remove();
            resolve(bool);
        }
    })
}
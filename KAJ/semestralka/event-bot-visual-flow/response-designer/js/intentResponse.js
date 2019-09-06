/**
 * Prvek odpovedi pro intent v designeru
 */
class IntentResponse{
    //init objektu a HTML prvku
    constructor(targetUl, prevIntent, intent = "", parent = null, subintents = []) {
        this.subintents = subintents; //array of IntentResponse
        this.responsesArray = [];
        this.parent = parent;
        this.parentUl = targetUl;
        this.liElement = getLiElement();
        this.prevIntentSpan = this.liElement.querySelector('.prev-intent');
        this.childrenUl = this.liElement.querySelector('.subintents-list');
        this.prevIntentSpan.textContent = prevIntent;
        const addButton = this.liElement.querySelector('.add-subintent-button');
        const removeButton = this.liElement.querySelector('.remove-subintent-button');
        this.intentSelect = this.liElement.querySelector('.intent-select');
        this.parentUl.appendChild(this.liElement);
        const responseTextArea = this.liElement.querySelector('.response-text');
        this.responsesArray.push(responseTextArea);
        if(intent.length > 0){
            this.intentSelect.value = intent;
        }
        addButton.addEventListener('click', this.addChild.bind(this));
        removeButton.addEventListener('click', this.removeIntentResponse.bind(this));
        this.intentSelect.addEventListener('change', this.onIntentChanged.bind(this));
        $(this.liElement).on('change keyup keydown paste cut', '.response-li > textarea', function (){
            $(this).height(0).height(this.scrollHeight);
            $(this).siblings('button').attr("disabled", $(this).val().length === 0);
        });

        $(this.liElement).on('click', 'ul > li > .btn-add', (e) => {
            e.stopPropagation();
            this.addResponse("");
        });
        $(this.liElement).on('click', 'ul > li > .btn-remove', this.removeResponseField.bind(this));
    }

    /**
     * Prida potomka aktualnimu intentu
     */
    addChild(){
        const intent = this.prevIntentSpan.textContent + this.intentSelect.value;
        const subintent = new IntentResponse(this.childrenUl, intent+'.', "", this);
        this.subintents.push(subintent);
    }

    /**
     * Odebere odpoved
     */
    removeIntentResponse(){
        if (this.parentUl.classList.contains('intents-list') && this.parentUl.childElementCount === 1) {
            alert("Hell no!");
            return;
        }
        //odebere subintent z rodice
        if(this.parent != null){
            const index = this.parent.subintents.indexOf(this);
            if (index > -1) {
                this.parent.subintents.splice(index, 1);
            }
        }

        //pokud je to korenovy prvek, odebere intent z korenoveho pole
        else{
            const index = rootList.indexOf(this);
            if (index > -1) {
                rootList.splice(index, 1);
            }
        }
        //odebere prvek z DOM
        this.liElement.remove();
    }

    /**
     * Aktualizuje retezec intentu v potomcich pri zmene intentu v rodicovi
     */
    onIntentChanged(){
        const intent = this.prevIntentSpan.textContent + this.intentSelect.value;
        for (let i = 0; i < this.subintents.length; i++) {
            const child = this.subintents[i];
            child.prevIntentChanged(intent);
        }
    }

    prevIntentChanged(completePrevIntent){
        this.prevIntentSpan.textContent = completePrevIntent;
        this.onIntentChanged();
    }

    /**
     * Prida odpoved k aktualnimu prvku
     * @param response
     */
    addResponse(response){
        const $lastLi = $(this.liElement).find('.responses:first').children('.response-li:last');
        const $formGroupClone = $lastLi.clone();
        if(response.length > 0){
            $lastLi.children('textarea').val(response);
        }
        else if($lastLi.children('textarea').val().length === 0){
            console.log('nope');
            return;
        }
        //nastavit remove button
        $lastLi.children('button').toggleClass('btn-success btn-add btn-danger btn-remove').html('–');
        $lastLi.children('button').attr('disabled', false);

        //pridat response na konec
        const textarea = $formGroupClone.children('textarea');
        textarea.val('');
        $formGroupClone.children('button').attr('disabled', true);
        $formGroupClone.insertAfter($lastLi);
        this.responsesArray.push(textarea[0]);
    }


    /**
     * Odebere odpoveda prislusne policko z aktualniho prvku
     * @param e
     */
    removeResponseField(e){
        const formGroup = $(e.target).closest('.response-li');
        const responseTextArea = $(e.target).siblings('.response-text')[0];

        const index = this.responsesArray.indexOf(responseTextArea);
        if (index > -1) {
            this.responsesArray.splice(index, 1);
        }

        formGroup.remove();
    }
}

/**
 * Generuje HTML prvek pro designer
 * @returns {ChildNode}
 */
function getLiElement() {
    let liTemplate = `<li class="intent-section bg-light mt-2 mb-2 ml-5 p-2">
                <div class="form-group">
                    <label class="d-block">Intent:
                        <span class="prev-intent text-muted">blh</span>
                        <select class="intent-select form-control form-control-sm" >
                           <option>root</option>
                            <option>refreshment</option>
                            <option>food</option>
                            <option>price</option>
                            <option>parking</option>
                            <option>date</option>
                            <option>place</option>
                            <option>transportation</option>
                            <option>drink</option>
                            <option>joke</option>
                            <option>besure</option>
                            <option>positive</option>
                        </select>
                    </label>
                </div>
                    <ul class="responses form-group multiple-form-group input-group p-0">
                        <li class="form-group multiple-form-group input-group response-li">
                            <textarea class="response-text form-control" placeholder="Odpověď..."></textarea>        
                            <button type="button" class="btn btn-success btn-add">+</button>          
                        </li class="form-group multiple-form-group input-group response-li">
                    </ul>
                <ul class="subintents-list list-unstyled">
                </ul>
                <button type="button" class="btn btn-sm btn-outline-primary add-subintent-button">+</button>
                <button type="button" class="btn btn-sm btn-outline-danger remove-subintent-button">-</button>
            </li>`;
    const template = document.createElement('template');
    liTemplate = liTemplate.trim();
    template.innerHTML = liTemplate;
    return template.content.firstChild;
}

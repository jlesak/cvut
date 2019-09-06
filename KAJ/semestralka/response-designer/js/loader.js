class FileLoader{
    constructor(){
        this.modalVisible = false;
        $('.modal-button').on('click',this.toggleModal.bind(this));

        const dropzone = document.querySelector("#drop_zone");
        dropzone.addEventListener("drop", e => this.handleDrop(e));
        dropzone.addEventListener("dragover", e => e.preventDefault());

        this.intentResponseMap = new Map(); //key: kompletní intent (food.price), value: IntentResponse object
    }

    /**
     * Zpracuje D'n'D soubor predany uzivatelem
     * @param ev
     */
    handleDrop(ev){
        ev.preventDefault();
        console.log('File(s) dropped');
        let file;
        if (ev.dataTransfer.items) {
            // Use DataTransferItemList interface to access the file(s)
            if(ev.dataTransfer.items.length > 1){
                alert('pouze jeden soubor!');
                return;
            }
            if (ev.dataTransfer.items[0].kind === 'file') {
                file = ev.dataTransfer.items[0].getAsFile();
            }
        }
        else {
            // Use DataTransfer interface to access the file(s)
            if(ev.dataTransfer.files.length > 1){
                alert('pouze jeden soubor!');
                return;
            }
            file = ev.dataTransfer.files[0];
        }
        this.readFile(file);
        this.toggleModal();
    }

    /**
     * Precte soubor
     * @param file
     */
    readFile(file){
        let reader = new FileReader();
        reader.onload = () => {
            const text = reader.result;
            // initOptions(this.getIntents(text));
            this.buildFromResponses(text);
        };

        reader.readAsText(file);
    }

    /**
     * Z JSON odpovedi vytvori strom odpovedi v designeru
     * @param stringResponses
     */
    buildFromResponses(stringResponses){
        const json = JSON.parse(stringResponses);
        const initIntentResponse = $('.intents-list').children();
        initIntentResponse.remove();
        this.intentResponseMap.clear();
        rootList = [];
        for (let i = 0; i < json.length; i++){
            const responseDefinition = json[i];
            this.parseResponseDefinition(responseDefinition)
        }
    }

    /**
     * Prevede jednotlivou odpoved na prvek designeru
     * @param responseDefinition
     */
    parseResponseDefinition(responseDefinition){
        let mapIntent;
        let intents = responseDefinition.intent.split('.');
        let prevIntent = "";
        let targetUl = document.querySelector('.intents-list');
        let parent = null;
        for (let i = 0; i < intents.length; i++){
            mapIntent = this.intentResponseMap.get(prevIntent + intents[i]);
            if(mapIntent === undefined) {
                mapIntent = new IntentResponse(targetUl, prevIntent, intents[i], parent);
                if(i === 0){
                    rootList.push(mapIntent);
                }
                if(parent != null){
                    parent.subintents.push(mapIntent);
                }
                this.intentResponseMap.set(prevIntent + intents[i], mapIntent);
            }
            prevIntent += intents[i] + '.';
            targetUl = mapIntent.childrenUl;
            parent = mapIntent;
        }
        mapIntent.addResponse(responseDefinition.payload);
    }

    /**
     * prepinac modalniho okna pro drag and drop
     */
    toggleModal() {
        this.modalVisible = !this.modalVisible;
        $('body').toggleClass('modal-visible');
        console.log("aasdasdasd");
    }
}

/**
 * Objekt reprezentujici intent a uzel ve stromu intentu
 */
class Intent {
    constructor(name) {
        this.name = name;
        this.children = [];
    }

    addChildNode(childTrieNode){
        this.children.push(childTrieNode);
    }
}

/**
 * Predpis pro export odpovedi do JSONu
 */
class ResponseDefinition{
    constructor(intentValue, response){
        this.topic = 'TestAgent';
        this.intent = intentValue;
        this.payloadType = 'text';
        this.payload = response;
        this.id = `response-definition:${this.generateGuid()}`;
    }

    generateGuid() {
        function s4() {
            return Math.floor((1 + Math.random()) * 0x10000)
                .toString(16)
                .substring(1);
        }
        return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
            s4() + '-' + s4() + s4() + s4();
    }
}

class JsonBuilder{
    constructor(){
        this.jsonBuilder = new JsonViewer();
    }

    /**
     * Exportuje a vypise odpovedi v JSONu do prislusne sekce
     * @returns {Array} pole odpovedi ResponseDefinition
     */
    exportResponses(){
        this.makeJsons();
        const target = document.querySelector('#responses-json');
        this.jsonBuilder.render(target, this.responses);
        return this.responses;
    }

    /**
     * Exportuje a vypise intenty do prislusne sekce
     * @returns {Intent} korenovy intent
     */
    exportIntents(){
        this.makeJsons();
        const target = document.querySelector('#intents-json');
        this.jsonBuilder.render(target, this.rootIntent);
        return this.rootIntent;
    }

    /**
     * Prevadi pole IntentResponse z designeru do stromu intentu a pole odpovedi
     */
    makeJsons() {
        this.responses = [];
        const rootIntent = new Intent("null");
        for (let i = 0; i < rootList.length; i++) {
            const child = rootList[i];
            rootIntent.addChildNode(this.parseIntentResponse(child));
        }
        this.rootIntent = rootIntent;
    }

    /**
     * Prevadi strom objektu IntentResponse z designeru na intenty a odpovedi
     * @param intentResponse jeden prvek IntentResponse z designeru
     * @returns {Intent} korenovy prvek stromu intentu daneho IntentResponse
     */
    parseIntentResponse(intentResponse){
        let prevIntent = intentResponse.prevIntentSpan.textContent;
        let nodeIntent = intentResponse.intentSelect.value;
        let intentValue = `${prevIntent}${nodeIntent}`;
        const trieNode = new Intent(nodeIntent);

        //responses
        for (let i = 0; i < intentResponse.responsesArray.length; i++) {
            intentResponse.responsesArray[i].value.trim();
            if(intentResponse.responsesArray[i].value !== ""){
                this.responses.push(new ResponseDefinition(intentValue, intentResponse.responsesArray[i].value));
            }
        }

        //intents
        const children = intentResponse.subintents;
        if (children.length === 0){
            return trieNode;
        }
        for (let i = 0; i < children.length; i++) {
            trieNode.addChildNode(this.parseIntentResponse(children[i]));
        }
        return trieNode;
    }
}

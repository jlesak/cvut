var rootList = [];
document.addEventListener('DOMContentLoaded', () => {
    new App();
});

class App{
    constructor() {
        this.builder = new JsonBuilder();
        this.loader = new FileLoader();
        this.vizualizer = new Visualizer();
        document.querySelector('.add-intent-button').addEventListener('click', App.addRootIntentResponse.bind(this));
        document.querySelector('#clear-btn').addEventListener('click', () => {
            rootList = [];
            localStorage.clear();
            document.querySelector('.intents-list').innerHTML = "";
            App.addRootIntentResponse();
        });

        App.addRootIntentResponse();

        const savedResponses = window.localStorage.getItem('savedResponses');
        if(savedResponses != null){
            this.loader.buildFromResponses(savedResponses);
        }

        this.pages = document.querySelectorAll(".page-section");
        this.buttons = document.querySelectorAll("#nav-list li a");
        this.route();
        // listen on url and history change
        window.addEventListener('popstate', () => {
            this.route();
        });
        const navButtons = document.querySelectorAll('#nav-list li a');
        for(const navButton of navButtons){
            navButton.addEventListener('click',e =>{
                $(e.target)
                    .toggleClass('active');
            });
        }
        setInterval(this.autosave.bind(this), 15000);
    }

    /**
     * Automaticke ukladani stavu aplikace v podobe odpovedi v JSON
     */
    autosave(){
        const responses = this.builder.exportResponses(true);
        if(responses.length > 0){
            localStorage.setItem('savedResponses', JSON.stringify(responses));
        }
    }

    /**
     * Prepinac navigace
      */
    route() {
        const hash = window.location.hash;
        this.changePage(hash);
        document.title = window.location.hash
    }

    /**
     * Prepne sekci podle hash v URL a vygeneruje pripadne JSONy a SVG
     * @param section
     */
    changePage (section) {
        this.autosave();
        switch (section) {
            case "#intents":
                this.builder.exportIntents();
                break;
            case "#responses":
                this.builder.exportResponses(false);
                break;
            case "#graph":
                const intents = this.builder.exportIntents();
                this.vizualizer.renderIntentsGraph(intents);
                break;
            default:
                section = "#designer";
                window.location.hash = section;
                document.title = window.location.hash;

                break;
        }

        this.pages.forEach(page => {
            if (page.getAttribute("data-route") === section) {
                page.classList.add("is-visible");
            } else {
                page.classList.remove("is-visible")
            }
        });

        this.styleButtons(section);
    }

    /**
     * Aktualizuje styly tlacitek pri prepnuti sekce
     * @param hash
     */
    styleButtons (hash){
        this.buttons.forEach(button => {
            if (button.getAttribute("href") === hash) {
                button.classList.add("active");
            } else {
                button.classList.remove("active")
            }
        })
    }

    /**
     * Prida korenovy prvek IntentResponse do korenoveho pole
     */
    static addRootIntentResponse(){
        const targetUl = document.querySelector('.intents-list');
        const initElement = new IntentResponse(targetUl, '');
        rootList.push(initElement);
    }
}


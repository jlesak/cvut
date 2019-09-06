/**
 * Trida pro vykresleni SVG stromu intentu
 */
class Visualizer {
    constructor(){
        this.svg = document.querySelector("#graph-svg");
        this.svgNs = "http://www.w3.org/2000/svg";

    }

    /**
     * vykresli strom intentu
     * @param rootIntent strom ktery ma byt vykresleny
     */
    renderIntentsGraph(rootIntent){
        this.leafs = 0;
        this.rows = 0;
        this.svg.innerHTML = "";
        this.iterateTree(rootIntent, 0);
        const width = 200+this.leafs*75;
        const height= 200+this.rows*100;
        this.svg.setAttribute('width', width);
        this.svg.setAttribute('height', height);
        this.renderTree(rootIntent, width/2, 50, width/2, 50)
    }


    renderTree(intent, x, y, parentX, parentY){
        let childX = x - (75 * (intent.children.length-1))/ 2;
        for (const child of intent.children) {
            this.renderTree(child, childX,y+100, x, y);
            childX+=75;
        }
        this.drawCircle(x, y, intent.name);
        this.drawLine(parentX, parentY, x, y);
    }


    /**
     * rekurzivne projde strom intentu a zjisti pocet listu v grafu
     * @param intent
     * @param row
     */
    iterateTree(intent, row){
        row++;
        if(this.rows < row) this.rows = row;
        intent.row = row;
        if(intent.children.length === 0){
            this.leafs++;
            return;
        }
        for (const child of intent.children){
            this.iterateTree(child, row)
        }
    }

    /**
     * Vykresli kolecko znacici uzel stromu a prida popisek
     * @param x
     * @param y
     * @param caption
     */
    drawCircle(x, y, caption){
        const circle = document.createElementNS(this.svgNs, "circle");
        circle.setAttributeNS(null, 'cx', x);
        circle.setAttributeNS(null, 'cy', y);
        circle.setAttributeNS(null, 'r', '2');
        circle.setAttributeNS(null, 'fill', 'red');
        this.svg.appendChild(circle);

        const text = document.createElementNS(this.svgNs, "text");
        text.setAttribute('x', x + 5);
        text.setAttribute('y', y);
        text.setAttribute('fill', '#000');
        text.textContent = caption;
        this.svg.appendChild(text);
    }

    /**
     * vykresli caru mezi rodicem a potomkem (dvema uzly)
     * @param parentX
     * @param parentY
     * @param childX
     * @param childY
     */
    drawLine(parentX, parentY, childX, childY) {
        const line = document.createElementNS(this.svgNs, "line");
        line.setAttribute("x1", parentX);
        line.setAttribute("y1", parentY);
        line.setAttribute("x2", childX);
        line.setAttribute("y2", childY);
        line.setAttribute("stroke", "black");
        this.svg.appendChild(line);
    }
}

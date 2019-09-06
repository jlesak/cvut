//
// Created by Jan on 01.12.2017.
//

#ifndef SEMESTRALKA_DIJKSTRA_H
#define SEMESTRALKA_DIJKSTRA_H


#include <string>
#include <limits>
#include <vector>

struct edge {
    edge(int sourceId, int targetId, int weight);
    int weight = 0; //vaha hrany
    int targetId = 0; // koncovy bod hrany
    int sourceId = 0; //pocatecni bod hrany
};

struct vertex {
    vertex(int id);
    int id = 0;
    int minDistance = std::numeric_limits<int>::max();
    vertex* previousVertex = nullptr;
    std::vector<edge*> edges = {};
};

class dijkstra {
public:
    /**
     * najde vertex s nejmensim minDistance. Projde vsechny vrcholy
     * @param vertexList
     * @return
     */
    vertex* getMin(std::vector<vertex*> vertexList);
    /***
     * Najde vertex podle id
     * @param id
     * @return
     */
    vertex* findVertex(int id);
    /***
     * vyresetuje cesty mezi vrcholy
     */
    void reset();
    /***
     * priradi vertexum jejich hrany
     * @param vertexList
     * @param edgesToVertexes
     */
    void createGraph(std::vector<vertex*> vertexList, std::vector<edge*> edgesToVertexes);
    std::vector<vertex *> getShortestPathTo(vertex* target);
    void computePath(vertex* source);
    std::vector<vertex *> getVertexes() const;
    std::vector<edge *> getEdges() const;

    ~dijkstra(){
        for (vertex *v : vertexes) {
            delete v;
        }
        for (edge *e : edges) {
            delete e;
        }
    }

private:
    std::vector<vertex*> vertexes;
    std::vector<edge*> edges;

};


#endif //SEMESTRALKA_DIJKSTRA_H

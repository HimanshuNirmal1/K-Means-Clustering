
import java.util.*;

/**
 * 612 Lab 5
 * Document clustering
 */
public class Clustering {
	
	int numDocs;
	int numClusters;
	int vSize;
	Doc[] docList;
	HashMap<String, Integer> termIdMap;
	
	ArrayList<Doc>[] clusters;
	Doc[] centroids;
	public Clustering(int numC)
	{
		numClusters = numC;
		clusters = new ArrayList[numClusters];
		centroids = new Doc[numClusters];
		termIdMap = new HashMap<String, Integer>();
	}
	
	/**
	 * Load the documents to build the vector representations
	 * @param docs
	 */
	public void preprocess(String[] docs){
		numDocs = docs.length;
		docList = new Doc[numDocs];
		int termId = 0;
		
		//collect the term counts, build term id map and the idf counts
		int docId = 0;
		for(String doc:docs){
			String[] tokens = doc.split(" ");
			Doc docObj = new Doc(docId);
			for(String token: tokens){
				if(!termIdMap.containsKey(token)){
					termIdMap.put(token, termId);
					docObj.termIds.add(termId);
					docObj.termWeights.add(1.0);					
					termId++;
				}
				else{
					Integer tid = termIdMap.get(token);
					int index = docObj.termIds.indexOf(tid);
					if (index >0){
						double tw = docObj.termWeights.get(index);
						docObj.termWeights.add(index, tw+1);
					}
					else{
						docObj.termIds.add(termIdMap.get(token));
						docObj.termWeights.add(1.0);
					}
				}
			}
			docList[docId] = docObj;
			docId++;
		}
		vSize = termId;
		//System.out.println("vSize: " + vSize);
		
		//compute the tf-idf weights of documents
		for(Doc doc: docList){
			double docLength = 0;
			double[] termVec = new double[vSize];
			for(int i=0;i<doc.termIds.size();i++){
				Integer tid = doc.termIds.get(i);
				double tfidf = (1+Math.log(doc.termWeights.get(i)));//Math.log(numDocs/idfMap.get(tid));				
				doc.termWeights.set(i, tfidf);
				docLength += Math.pow(tfidf, 2);
			}
			
			//normalize the doc vector			
			for(int i=0;i<doc.termIds.size();i++){
				double tw = doc.termWeights.get(i);
				doc.termWeights.set(i, tw/docLength);
				//System.out.println(doc.termIds.get(i));
				termVec[doc.termIds.get(i)] = tw/docLength;
			}
			doc.termVec = termVec;
			//doc.termIds = null;
			//doc.termWeights = null;
		}
	}
	
	/**
	 * Cluster the documents
	 * For kmeans clustering, use the first and the ninth documents as the initial centroids
	 */
	public void cluster(){
		//TO BE COMPLETED
		
 	}
	

	
	public static void main(String[] args){
		String[] docs = {"hot chocolate cocoa beans",
				 "cocoa ghana africa",
				 "beans harvest ghana",
				 "cocoa butter",
				 "butter truffles",
				 "sweet chocolate can",
				 "brazil sweet sugar can",
				 "suger can brazil",
				 "sweet cake icing",
				 "cake black forest"
				};
		Clustering c = new Clustering(2);
		c.preprocess(docs);
		System.out.println("Vector space representation:");
		for(int i=0;i<c.docList.length;i++){
			System.out.println(c.docList[i]);
		}
		
		c.cluster();
	}
}

/**
 * 
 * Document id class that contains the document id and the term weight in tf-idf
 */
class Doc{
	int docId;
	ArrayList<Integer> termIds;
	ArrayList<Double> termWeights;
	double[] termVec;
	public Doc(){
		
	}
	public Doc(int id){
		docId = id;
		termIds = new ArrayList<Integer>();
		termWeights = new ArrayList<Double>();
	}
	public void setTermVec(double[] vec){
		termVec = vec;
	}
   
	public String toString()
	{
		String docString = "[";
		for(int i=0;i<termVec.length;i++){
			docString += termVec[i] + ",";
		}
		return docString+"]";
	}
	
}
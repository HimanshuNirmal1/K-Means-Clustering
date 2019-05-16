

/**

ISTE 612-01
Himanshu Nirmal
Lab 5

*/



public class Clustering {
	
	Doc[] dVector=new Doc[10];
	String[] docsIn;
	String[] cltr=new String[2];
	Doc c[];
	
   
   //initialization
	public Clustering(int numC)
	{
		c=new Doc[numC];
	}

	/**
	 * Load the documents to build the vector representations
	 * @param docs: input from main
	 */
	public void preprocess(String[] docs){
		docsIn=docs;
		for(int i=0;i<docs.length;i++){
			String[] doc=docs[i].split(" ");
			dVector[i]=new Doc(i,tidScore(doc));
					}
		
		c[0]=new Doc(0,dVector[0].data);
		c[1]=new Doc(9,dVector[9].data);
	}
	private double[] tidScore(String[] doc) {
		double tf[]=tfVal(doc);
		double idf[]=new double[4];
		double tiScore[]=new double[4];
		
         
        int i=0;
        while(i<doc.length){
			idf[i]=tfIdf(doc[i]);
			tiScore[i]=tf[i]*idf[i];
			
		   i++;
         }
		return tiScore;
	}


	private double tfIdf(String term) {
		double n = 0;
		for(String w1:docsIn){
			String[] x=w1.split(" ");
			for(String w:x){
				if(w.equalsIgnoreCase(term)){
					n++;
					break;
				}
			}
		}

		return Math.log(docsIn.length/n);
	}




	/**
	 * tfVal : return value for tf
	 * @param doc: split doc
	 * 
	 */
	private double[] tfVal(String[] doc) {
		double c=0;
		String l1="";
		double score[]=new double[4];
			int i=0;
         while(i<doc.length){
         l1=doc[i];
			for(String l2:doc){
				if(l1.equals(l2))
					c++;
			}
			score[i]=c/doc.length;
			c=0;
         i++;
		}
		return score;
	}
	

   
   
   /**
	 * getCenter: re-calculate the new center of given cluster points.
	 * @param dt
	 * 
	 */
    
	private double[] getCenter(double[] dt) {
		double[] c=new double[4];
		for(int m=0;m<dt.length;m++){
			if(dt[m]!=0){
	               int n=0;
      				while(n<4){
               c[n]+=dVector[m].data[n];
					
				   n++;
               }
			}
			

		}
		int k=0;
             while( k!=4){
			c[k]=c[k]/dt.length;
				      k++;	
          }
		
		return c;
	} 

	/**
	 * 
	 * cluster the documents
	 * For kmeans clustering, use the first and the ninth documents as the initial centroids
	 */
	public void cluster(){
		System.out.println("Clusters: ");
		int noitr=0;
		Doc clusters[]=new Doc[2];
		
      for(noitr=0;noitr<5;noitr++){
		
			
            int i =0;
            while(i<c.length){
				double[] l1 = new double[20];
				for(int j=0;j<dVector.length;j++){					
					l1[j]=cosSim(c[i].data,dVector[j].data);
					
				}
				
			
         	clusters[i]=new Doc(i,l1);
			   i++;
         }
			String s1="-";
			String s2="-";
			double[] dt1=clusters[0].data;
			double[] dt2=clusters[1].data;
			cltr[0]="Cluster 0:	 \n";
			cltr[1]="Cluster 1: 	\n";
			
			
            int k=0;
            while (k!=4){
				if(dt1[k]<dt2[k]){
					dt1[k]=0;
									}
				else{
					dt2[k]=0;
					
				}

				s1+="|(0,"+k+"): "+dt1[k];
				s2+="|(1,"+k+"): "+dt2[k];
            k++;
			}
			
         
            int z=0;
            while(z!=4){
         	cltr[0]+=z+" ";				
				cltr[1]+=(z+5)+" ";
            z++;
            }
			
			
			c[0].data=getCenter(dt1);
			c[1].data=getCenter(dt2);
		}
		System.out.println(cltr[0]);
		System.out.println(cltr[1]);
	}
	

	/**
	 * cosSim: get Cosine similarity from 2 doc based on input tf score
	 * @param a: score for d1
	 * @param b: score for d2
	 */
	private double cosSim(double[] a, double[] b) {	
		double dp=0; 
      double agm=0, bgm=0;
		for(int i=0;i<((a.length<b.length)?a.length:b.length);i++){
			dp+=a[i]*b[i];
			agm+=Math.pow(a[i],2);
			bgm+=Math.pow(b[i],2);
		}
		agm = Math.sqrt(agm);
		bgm = Math.sqrt(bgm);
		double d = dp / (agm * bgm);
		return d==Double.NaN?0:d;
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

		c.cluster();
		
	}
}

/**
 * 
 * Document id class that contains the document id and the term weight in tf-idf
 */

class Doc{
	int id;
	double[] data;
	int cId;
	public Doc(int n,double[] d){
		id=n;
		data=d;
	}
	public Doc(int n){
		id=n;
	}
	public void setCluster(int ic){
		cId=ic;
	}
	public String toString(){
		String s=": doc id - "+(id+1);
		for(double d:data){
			s+=" "+d;
		}
		return s;
	}
}
using System;
using System.Net;
using System.Collections.Generic;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using System.Collections.ObjectModel;
using System.Threading.Tasks;
using Windows.Storage;
using System.IO;
using System.Diagnostics;
using System.Xml.Linq;
using Sparrow.Chart;
using System.Device.Location;

namespace ClimateInMyN1
{
    public partial class MainPage : PhoneApplicationPage
    {        
        // Constructor
        public MainPage()
        {
            InitializeComponent();
            myMap.Center = new GeoCoordinate(35, 33);
            myMap.ZoomLevel = 8;
            Random r=new Random();

            ObservableCollection<Model> tmpCollection = new ObservableCollection<Model>();
            readClimateAnomaly(tmpCollection);

            ObservableCollection<Model> tmpCollection2 = new ObservableCollection<Model>();
            readGlobalAverageTemp(tmpCollection2);

            ObservableCollection<Model> tmpCollection3 = new ObservableCollection<Model>();
            ObservableCollection<Model> tmpCollection4 = new ObservableCollection<Model>();
            readGreenhouse(tmpCollection3, tmpCollection4);

            ObservableCollection<Model> tmpCollection5 = new ObservableCollection<Model>();
            ObservableCollection<Model> tmpCollection6 = new ObservableCollection<Model>();            
            foreach(Model m in tmpCollection){
                if(m.XData%2==0)tmpCollection5.Add(new Model(m.XData,m.YValue+15));
                if (m.XData % 3 == 0) tmpCollection6.Add(new Model(m.XData, m.YValue + 60));
            }
            chart.Series = load(tmpCollection);
            chart.OnApplyTemplate();

            chart2.Series = load2(tmpCollection2);
            chart2.OnApplyTemplate();

            chart3.Series = load3(tmpCollection3,tmpCollection4);
            chart3.OnApplyTemplate();

            chart4.Series = load4(tmpCollection5);
            chart4.OnApplyTemplate();

            chart5.Series = load5(tmpCollection6);
            chart5.OnApplyTemplate();
        }

        private SeriesCollection load(ObservableCollection<Model> tmpCollection)
        {
            ObservableCollection<Model> Collection;
            SeriesCollection LineSeries;

            LineSeries = new SeriesCollection();
            Collection = new ObservableCollection<Model>();
            //readClimateAnomaly(Collection);
            Collection = tmpCollection;
            LineSeries Series1 = new LineSeries();
            Series1.XPath = "XData";
            Series1.YPath = "YValue";
            Series1.PointsSource = Collection;

            LineSeries.Add(Series1);
            return LineSeries;
        }

        private SeriesCollection load2(ObservableCollection<Model> tmpCollection)
        {
            ObservableCollection<Model> Collection;
            SeriesCollection LineSeries;

            LineSeries = new SeriesCollection();
            Collection = new ObservableCollection<Model>();
            //readClimateAnomaly(Collection);
            Collection = tmpCollection;
            ColumnSeries Series1 = new ColumnSeries();
            Series1.XPath = "XData";
            Series1.YPath = "YValue";
            Series1.PointsSource = Collection;

            LineSeries.Add(Series1);
            return LineSeries;
        }

        private SeriesCollection load3(ObservableCollection<Model> tmpCollection,ObservableCollection<Model> tmpCollection2)
        {
            ObservableCollection<Model> Collection;
            SeriesCollection LineSeries;

            LineSeries = new SeriesCollection();
            Collection = new ObservableCollection<Model>();
            //readClimateAnomaly(Collection);
            Collection = tmpCollection;
            ScatterSeries Series1 = new ScatterSeries();
            Series1.XPath = "XData";
            Series1.YPath = "YValue";
            Series1.PointsSource = Collection;
            LineSeries.Add(Series1);

            Collection = tmpCollection2;
            Series1 = new ScatterSeries();
            Series1.XPath = "XData";
            Series1.YPath = "YValue";
            Series1.PointsSource = Collection;
            LineSeries.Add(Series1);

            return LineSeries;
        }

        private SeriesCollection load4(ObservableCollection<Model> tmpCollection)
        {
            ObservableCollection<Model> Collection;
            SeriesCollection LineSeries;

            LineSeries = new SeriesCollection();
            Collection = new ObservableCollection<Model>();
            //readClimateAnomaly(Collection);
            Collection = tmpCollection;
            SplineSeries Series1 = new SplineSeries();
            Series1.XPath = "XData";
            Series1.YPath = "YValue";
            Series1.PointsSource = Collection;

            LineSeries.Add(Series1);
            return LineSeries;
        }

        private SeriesCollection load5(ObservableCollection<Model> tmpCollection)
        {
            ObservableCollection<Model> Collection;
            SeriesCollection LineSeries;

            LineSeries = new SeriesCollection();
            Collection = new ObservableCollection<Model>();
            //readClimateAnomaly(Collection);
            Collection = tmpCollection;
            StepLineSeries Series1 = new StepLineSeries();
            Series1.XPath = "XData";
            Series1.YPath = "YValue";
            Series1.PointsSource = Collection;

            LineSeries.Add(Series1);
            return LineSeries;
        }

        public async void readClimateAnomaly(ObservableCollection<Model> ocm)
        {            
            var contents = await ReadFileContentsAsync("climateanomaly.xml");
            //Debug.WriteLine(contents);

            XDocument extractedContents = XDocument.Parse(contents);
            foreach (var childElem in extractedContents.Descendants().Where(p => p.Name.LocalName == "data"))
            {
                string childName = childElem.Element("year").Value;
                string childValue = childElem.Element("value").Value;
                //Debug.WriteLine(childName+","+childValue);

                try
                {
                    Sparrow.Chart.DoublePoint pi = new Sparrow.Chart.DoublePoint();
                    
                    double d=double.Parse(childName, System.Globalization.NumberStyles.AllowLeadingSign);                                                            

                    double v;
                    if (childValue.Contains('-'))
                    {                        
                        v = double.Parse(childValue.Replace("-", ""));
                        v *= -1;
                    }
                    else
                    {
                        v = double.Parse(childValue);
                    }
                    //Debug.WriteLine(childName+", "+childValue);

                    pi.Data = d; 
                    pi.Value = v;
                    ocm.Add(new Model(d, v));                            
                }
                catch (Exception e)
                {
                    Debug.WriteLine(e);
                }                
            }
        }

        public void readGlobalAverageTemp(ObservableCollection<Model> ocm)
        {
            //var contents = await ReadFileContentsAsync("globalavertemp.txt");            
            //Debug.WriteLine(contents);
            using (var reader = new StreamReader(@"Assets\globalavertemp.txt",true))
            {
                string line;
                while ((line = reader.ReadLine()) != null)
                {
                    string[] s = line.Split(',');
                    double avg=0;
                    for (int i = 1; i < s.Length; i++)
                    {
                        avg+=double.Parse(s[i]);
                    }
                    ocm.Add(new Model(double.Parse(s[0]),avg/line.Length-1));
                    //Debug.WriteLine(ocm.Last().XData +","+ ocm.Last().YValue);
                }
            }
        }

        public void readGreenhouse(ObservableCollection<Model> ocm, ObservableCollection<Model> ocm2)
        {

            //Year,CO2,CH4,N2O,CFC12,CFC11,15-minor,Total,CO2-eq (ppm) Total,AGGI 1990 = 1,AGGI % change
            using (var reader = new StreamReader(@"Assets\greenhouse.txt", true))
            {
                string line;
                while ((line = reader.ReadLine()) != null)
                {
                    string[] s = line.Split(',');
                    ocm.Add(new Model(double.Parse(s[0]), double.Parse(s[1])));
                    ocm2.Add(new Model(double.Parse(s[0]), double.Parse(s[7])));
                    //Debug.WriteLine(ocm.Last().XData + "," + ocm.Last().YValue);
                    //Debug.WriteLine(ocm2.Last().XData + "," + ocm2.Last().YValue);
                }
            }
        }

        public ObservableCollection<Model> Collection { get; set; }
        public SeriesCollection LineSeries { get; set; }

        //Model Class
        public class Model
        {
            public double XData { get; set; }
            public double YValue { get; set; }
            public Model(double xData, double yValue)
            {
                this.XData = xData;
                this.YValue = yValue;
            }
        }   

        public async Task<string> ReadFileContentsAsync(string fileName)
        {
            try
            {
                StorageFile sFile = await Windows.ApplicationModel.Package.Current.InstalledLocation.GetFileAsync(@"Assets\" + fileName);

                var fileStream = await sFile.OpenStreamForReadAsync();                   

                using (var streamReader = new StreamReader(fileStream))
                {
                    return streamReader.ReadToEnd();
                }
            }
            catch (Exception)
            {
                return string.Empty;
            }
        }

        // Load data for the ViewModel Items
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            if (!App.ViewModel.IsDataLoaded)
            {
                App.ViewModel.LoadData();
            }
        }

    }
}
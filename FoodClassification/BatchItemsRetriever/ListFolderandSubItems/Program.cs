using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace ListFolderandSubItems
{
    class Program
    {
        static void Main(string[] args)
        {
            //specify directory in arg0 and type of file in arg1, will return a list of files with direct path 
            string[] allfiles = Directory.GetFiles(args[0], "*."+args[1], SearchOption.AllDirectories);
            Console.WriteLine("Directory : "+ args[0]);
            Console.WriteLine("File Type : *." + args[1]);
            Console.WriteLine("----------------------------");
            foreach (string file in allfiles)
                System.Console.WriteLine(file);
        }
        

    }
}

using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebOfThingsPlatform
{
    public class Program
    {
        public static void Main(string[] args)
        {
            PrepareDatabase();
            CreateHostBuilder(args).Build().Run();
        }

        public static IHostBuilder CreateHostBuilder(string[] args) =>
            Host.CreateDefaultBuilder(args)
                .ConfigureWebHostDefaults(webBuilder =>
                {
                    webBuilder.UseStartup<Startup>();
                });

        public static void PrepareDatabase()
        {
            // define a connection string
            /*const string connectionString = "type=embedded;storesdirectory=.\\;storename=WET";


            // if the store does not exist it will be automatically
            // created when a context is created
            var ctx = new MyEntityContext(connectionString);


            // create some films
            var bladeRunner = ctx.Films.Create();
            bladeRunner.Name = "BladeRunner";


            var starWars = ctx.Films.Create();
            starWars.Name = "Star Wars";


            // create some actors and connect them to films
            var ford = ctx.Actors.Create();
            ford.Name = "Harrison Ford";
            ford.DateOfBirth = new DateTime(1942, 7, 13);
            ford.Films.Add(starWars);
            ford.Films.Add(bladeRunner);


            var hamill = ctx.Actors.Create();
            hamill.Name = "Mark Hamill";
            hamill.DateOfBirth = new DateTime(1951, 9, 25);
            hamill.Films.Add(starWars);


            // save the data
            ctx.SaveChanges();


            // open a new context, not required
            ctx = new MyEntityContext(connectionString);


            // find an actor via LINQ
            ford = ctx.Actors.Where(a => a.Name.Equals("Harrison Ford")).FirstOrDefault();
            var dob = ford.DateOfBirth;


            // list his films
            var films = ford.Films;


            // get star wars
            var sw = films.Where(f => f.Name.Equals("Star Wars")).FirstOrDefault();


            // list actors in star wars
            foreach (var actor in sw.Actors)
            {
                var actorName = actor.Name;
                Console.WriteLine(actorName);
            }

            Console.ReadLine();*/
        }
    }
}

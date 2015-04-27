using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(JiFriend.Startup))]
namespace JiFriend
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
